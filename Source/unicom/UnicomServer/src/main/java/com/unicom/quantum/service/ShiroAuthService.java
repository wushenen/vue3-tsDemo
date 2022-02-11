package com.unicom.quantum.service;

import java.util.List;

public interface ShiroAuthService {
    List<Integer> getGroupInfosByDeviceId(int deviceId);
}
