package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DTO.TempKeyDTO;
import com.cucc.unicom.pojo.Random;


public interface GenerateRandomService {
    TempKeyDTO generateTempKey(Random random) throws Exception;
}
