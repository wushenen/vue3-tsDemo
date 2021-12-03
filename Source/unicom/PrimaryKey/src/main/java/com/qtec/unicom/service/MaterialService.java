package com.qtec.unicom.service;

import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.pojo.Material;
import com.qtec.unicom.pojo.dto.MaterialDTO;

public interface MaterialService {

    MaterialDTO getParametersForImport(Material material) throws Exception;

    Object importKeyMaterial(Material material)throws Exception;

    void deleteKeyMaterial(Material material)throws Exception;

    boolean checkExternal(String keyId) throws PwspException;
}
