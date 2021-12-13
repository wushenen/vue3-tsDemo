package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.TempKeyMapper;
import com.cucc.unicom.pojo.Random;
import com.cucc.unicom.pojo.dto.TempKeyDTO;
import com.cucc.unicom.component.Exception.PwspException;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.component.util.UtilService;
import com.cucc.unicom.service.GenerateRandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
public class GenerateRandomServiceImpl implements GenerateRandomService {
    @Autowired
    private TempKeyMapper tempKeyMapper;
    @Autowired
    private UtilService utilService;

    @OperateLogAnno(operateDesc = "生成", operateModel = "密钥管理模块")
    @Override
    public TempKeyDTO generateTempKey(Random random) throws Exception {
        Integer keyLen = random.getKeyLen();
        keyLen = keyLen ==null?32:keyLen;
        TempKeyDTO dto = new TempKeyDTO();
        String keyId = random.getKeyId()==null? UUID.randomUUID().toString():random.getKeyId();
        dto.setKeyId(keyId);
        if (keyLen >= 8 || keyLen <= 128) {
            byte[] tempKey = utilService.generateQuantumRandom(keyLen);
            dto.setTempKey(Base64.getEncoder().encodeToString(tempKey));
            random.setTempKey(tempKey);
            random.setKeyId(keyId);
            tempKeyMapper.addAppSecret(random);
        } else {
            throw new PwspException(ResultHelper.genResult(400, "InvalidParameter", "口令字节数长度应为8~128"));
        }
        return dto;
    }
}
