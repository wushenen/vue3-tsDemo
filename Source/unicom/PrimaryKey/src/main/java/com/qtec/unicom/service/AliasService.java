package com.qtec.unicom.service;

import com.qtec.unicom.pojo.Alias;

import java.util.List;

public interface AliasService {
    Alias createAlias(Alias alias) throws Exception;

    void updateAlias(Alias alias) throws Exception;

    void deleteAlias(Alias alias)throws Exception;

    List<Alias> listAliases(String userName) throws Exception;

    List<Alias> listAliasesByKeyId(Alias alias) throws Exception;
}
