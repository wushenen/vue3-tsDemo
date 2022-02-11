package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.mapper.TempKeyMapper;
import com.unicom.quantum.pojo.Random;
import com.unicom.quantum.pojo.DTO.TempKeyDTO;
import com.unicom.quantum.service.GenerateRandomService;
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

    @OperateLogAnno(operateDesc = "生成临时量子密钥", operateModel = "密钥管理模块")
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
            throw new QuantumException(ResultHelper.genResult(1, "口令字节数长度应为8~128"));
        }
        return dto;
    }
}
