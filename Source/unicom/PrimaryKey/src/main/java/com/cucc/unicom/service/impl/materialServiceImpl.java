package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.util.*;
import com.cucc.unicom.mapper.KeyVersionMapper;
import com.cucc.unicom.mapper.MaterialMapper;
import com.cucc.unicom.mapper.PrimaryKeyMapper;
import com.cucc.unicom.pojo.KeyVersion;
import com.cucc.unicom.pojo.Material;
import com.cucc.unicom.pojo.PrimaryKey;
import com.cucc.unicom.pojo.dto.MaterialDTO;
import com.cucc.unicom.service.MaterialService;
import com.cucc.unicom.service.PrimaryKeyService;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.Exception.PwspException;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.sansec.device.bean.SM2refPublicKey;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;

@Service
public class materialServiceImpl implements MaterialService {

    private final String OPERATE_MODEL = "密钥管理模块";

    private static Long expTime = 24*60*60*1000L;
    @Autowired
    private PrimaryKeyMapper primaryKeyMapper;
    @Autowired
    private PrimaryKeyService primaryKeyService;
    @Autowired
    private KeyVersionMapper keyVersionMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Value("${cmdPath}")
    private String cmdPath;
    /**
     * 默认index为1的非对称加密密钥来做加解密
     * @param material
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "获取密钥材料", operateModel = OPERATE_MODEL)
    @Override
    public MaterialDTO getParametersForImport(Material material) throws Exception {
        isExternal(material.getKeyId());
        String importToken = JWTUtil.generateToken(material.getKeyId(),expTime);
        MaterialDTO dto = new MaterialDTO();
        switch (material.getWrappingKeySpec()){
            case "EC_SM2":
                //默认第一组加密密钥对
                SM2refPublicKey pubKey = SwsdsTools.getSM2PublicKey(1,1);
                String pubHex = HexUtils.bytesToHexString(pubKey.getX())+HexUtils.bytesToHexString(pubKey.getY());
                Encoder encoder = Base64.getEncoder();
                material.setPublicKey(encoder.encodeToString(pubHex.getBytes("UTF-8")));
                BeanUtils.copyProperties(dto, material);
                dto.setImportToken(importToken);
                Date exp = new Date(System.currentTimeMillis() + expTime);
                dto.setTokenExpireTime(DateUtils.getZTimeStr(exp));
                if(!"SM2PKE".equalsIgnoreCase(material.getWrappingAlgorithm())){
                    throw new PwspException(ResultHelper.genResult(400,"<加密算法>无效"));
                }
                break;
                default:
                    throw new PwspException(ResultHelper.genResult(400,"参数“<wrappingKeySpec>”是必需的，但未提供"));

        }
        //判断材料是不是之前获取的
        Material mat = materialMapper.selectMaterial(material.getKeyId());
        if(mat == null){
            //新增材料入库
            materialMapper.addMaterial(material);
        }
        return dto;
    }

    /**
     * 导入外部密钥材料
     * 默认第一组加密密钥对
     * @param material
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "导入外部密钥材料", operateModel = OPERATE_MODEL)
    @Override
    public Object importKeyMaterial(Material material) throws Exception {
        isExternal(material.getKeyId());
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(material.getKeyId());
        primaryKeyService.keyStatsEnable(primaryKey,3);
        primaryKey.setDeleteDate(null);
        primaryKey.setKeyState("Enabled");
//        isExternal(material.getKeyId());
        //判断令牌时间和签名
        String importToken = material.getImportToken();
        Long jwtDate = JWTUtil.getExpiresAt(importToken).getTime();
        if(jwtDate < System.currentTimeMillis()){
            throw new PwspException(ResultHelper.genResult(400,"导入令牌已过期"));
        }
        if(!JWTUtil.verifyToken(importToken,material.getKeyId())){
            throw new PwspException(ResultHelper.genResult(400,"导入令牌无效"));
        }
        //判断材料是不是之前获取的
        Material mat = materialMapper.selectMaterial(material.getKeyId());
        if(mat == null){
            throw new PwspException(ResultHelper.genResult(400,"请重新获取密钥材料"));
        }

        KeyVersion keyVersion = new KeyVersion();
        keyVersion.setKeyId(material.getKeyId());
        keyVersion = keyVersionMapper.getKeyVersion(keyVersion);
        //默认第一组加密密钥对
        byte[]k;
        try{
            k = SwsdsTools.sm2Decrypt1(2,Base64.getDecoder().decode(material.getEncryptedKeyMaterial()));
        }catch (Exception e){
            e.printStackTrace();
            throw new PwspException(ResultHelper.genResult(400,"密钥材料无效"));
        }
        //材料解密，导入密码卡
        int index = 0;
        int isAdd = 1;
        if(keyVersion.getCardIndex() == null){
            index = SwsdsTools.getKeyIndex();
        }else{
            isAdd = 2;
            index = keyVersion.getCardIndex();
        }
        String ml = "sh "+ cmdPath +"importPrimaryKey.sh "+isAdd+" "+index+" "+ new String(k,"UTF-8");
        CmdUtil.executeLinuxCmd(ml);
        //新增材料入库
        materialMapper.updateMaterial(material);
        keyVersion.setCardIndex(index);
        //更新密钥状态
        primaryKeyMapper.updatePrimaryKeyState(primaryKey);
        int num = keyVersionMapper.updateKeyVersionCardIndex(keyVersion);
        return num;
    }

    /**
     * 1.此操作不会删除其对应的主密钥（CMK）
     * 2.如果 CMK 处于待删除状态，删除密钥材料不会改变密钥状态和预计删除时间；
     * 3.如果密钥不是处于待删除状态，删除密钥材料会使得密钥状态变更为等待导入。
     * @param material
     * @return
     * @throws Exception
     */
    @Transactional
    @OperateLogAnno(operateDesc = "删除密钥材料", operateModel = OPERATE_MODEL)
    @Override
    public void deleteKeyMaterial(Material material) throws Exception {
        isExternal(material.getKeyId());
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(material.getKeyId());
        if(!"PendingDeletion".equalsIgnoreCase(primaryKey.getKeyState())){
            primaryKey.setKeyState("PendingImport");
            primaryKeyMapper.updatePrimaryKeyState(primaryKey);
            materialMapper.deleteMaterial(material);
        }
    }

    @Override
    public boolean checkExternal(String keyId) throws PwspException {
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(keyId);
        if (primaryKey == null)
            throw new PwspException(ResultHelper.genResult(1,"此密钥不存在"));
        if(!"EXTERNAL".equalsIgnoreCase(primaryKey.getOrigin()))
            return false;
        return true;
    }

    /**
     * 判断密钥是否来源外部
     * @param keyId
     * @return
     * @throws PwspException
     */
    public void isExternal(String keyId) throws PwspException {
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(keyId);
        //判断密钥来源是否符合
        if(!"EXTERNAL".equalsIgnoreCase(primaryKey.getOrigin())){
            throw new PwspException(ResultHelper.genResult(400,"此密钥来源对此接口无效"));
        }
    }
}




