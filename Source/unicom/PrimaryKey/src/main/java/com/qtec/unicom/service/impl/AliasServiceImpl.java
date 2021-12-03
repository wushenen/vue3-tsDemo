package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.mapper.AliasMapper;
import com.qtec.unicom.pojo.Alias;
import com.qtec.unicom.service.AliasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasServiceImpl implements AliasService {

    private final String OPERATE_MODEL = "别名管理模块";

    @Autowired
    private AliasMapper aliasMapper;
    /**
     * 别名必须唯一
     * @param alias
     * @return
     */
    @OperateLogAnno(operateDesc = "创建别名", operateModel = OPERATE_MODEL)
    @Override
    public Alias createAlias(Alias alias) throws Exception{
        Alias reAlias = aliasMapper.getAlias(alias.getAliasName());
        if(reAlias != null){
            throw new PwspException(ResultHelper.genResult(400,"AliasAlreadyExists","别名已存在"));
        }
        aliasMapper.addAlias(alias);
        return alias;
    }
    @OperateLogAnno(operateDesc = "更新别名", operateModel = OPERATE_MODEL)
    @Override
    public void updateAlias(Alias alias) throws Exception {
        Alias reAlias = aliasMapper.getAlias(alias.getAliasName());
        if(reAlias == null){
            throw new PwspException(ResultHelper.genResult(404,"找不到指定别名","Forbidden.AliasNotFound"));
        }
        aliasMapper.updateAlias(alias);
    }
    @OperateLogAnno(operateDesc = "删除别名", operateModel = OPERATE_MODEL)
    @Override
    public void deleteAlias(Alias alias) throws Exception {
        aliasMapper.deleteAlias(alias.getAliasName());
    }
    @OperateLogAnno(operateDesc = "列出所有别名", operateModel = OPERATE_MODEL)
    @Override
    public List<Alias> listAliases(String userName) throws Exception {
        Alias alias = new Alias();
        alias.setOwner(userName);
        return aliasMapper.listAlias(alias);
    }

    @Override
    public List<Alias> listAliasesByKeyId(Alias alias) throws Exception {
        return aliasMapper.listAlias(alias);
    }
}




