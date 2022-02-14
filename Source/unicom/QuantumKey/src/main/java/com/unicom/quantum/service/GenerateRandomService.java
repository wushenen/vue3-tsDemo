package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.GenerateTempKeyRequest;
import com.unicom.quantum.pojo.DTO.TempKeyDTO;


public interface GenerateRandomService {
    TempKeyDTO generateTempKey(GenerateTempKeyRequest generateTempKeyRequest) throws Exception;
}
