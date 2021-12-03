package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.KeyOfflineDTO;
import com.qtec.unicom.pojo.KeyOffline;

import java.util.List;

public interface KeyOfflineService {

    void addOffKey(int number) throws Exception;

    List<KeyOfflineDTO> getOffKey(Long start, Long end) throws Exception;

}
