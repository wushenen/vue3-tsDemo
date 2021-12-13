package com.cucc.unicom.service;

import com.cucc.unicom.pojo.Random;
import com.cucc.unicom.pojo.dto.TempKeyDTO;


public interface GenerateRandomService {
    TempKeyDTO generateTempKey(Random random) throws Exception;
}
