package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DTO.KeyOfflineDTO;

import java.util.List;

public interface KeyOfflineService {

    void addOffKey(int number) throws Exception;

    List<KeyOfflineDTO> getOffKey(Long start, Long end) throws Exception;

}
