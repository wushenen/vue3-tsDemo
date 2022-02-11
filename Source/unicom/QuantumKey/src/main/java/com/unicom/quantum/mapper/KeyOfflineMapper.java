package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.KeyOffline;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KeyOfflineMapper {

    void addOffKey(KeyOffline keyOffline);

    List<KeyOffline> getOffKey(Long start, Long end);

    Long countOfflineKeyNum();

}
