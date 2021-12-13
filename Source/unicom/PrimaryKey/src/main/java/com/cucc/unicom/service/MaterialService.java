package com.cucc.unicom.service;

import com.cucc.unicom.pojo.Material;
import com.cucc.unicom.pojo.dto.MaterialDTO;
import com.cucc.unicom.component.Exception.PwspException;

public interface MaterialService {

    MaterialDTO getParametersForImport(Material material) throws Exception;

    Object importKeyMaterial(Material material)throws Exception;

    void deleteKeyMaterial(Material material)throws Exception;

    boolean checkExternal(String keyId) throws PwspException;
}
