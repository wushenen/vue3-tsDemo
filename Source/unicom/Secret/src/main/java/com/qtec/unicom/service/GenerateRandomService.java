package com.qtec.unicom.service;

import com.qtec.unicom.pojo.Random;
import com.qtec.unicom.pojo.dto.TempKeyDTO;


public interface GenerateRandomService {
    TempKeyDTO generateTempKey(Random random) throws Exception;
}
