package com.unicom.quantum.service;

import com.unicom.quantum.pojo.Random;
import com.unicom.quantum.pojo.DTO.TempKeyDTO;


public interface GenerateRandomService {
    TempKeyDTO generateTempKey(Random random) throws Exception;
}
