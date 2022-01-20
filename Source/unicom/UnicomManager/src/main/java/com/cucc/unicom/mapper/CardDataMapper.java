package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.CardData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CardDataMapper {
    @Insert("insert into t_card_data(card_version,card_data,sql_data,mac_addr)" +
            " values(#{cardData.cardVersion}, #{cardData.cardData}, #{cardData.sqlData}, #{cardData.macAddr})")
    int addCardData(@Param("cardData") CardData cardData);
    @Select({"<script>" +
            "select card_version,card_data,mac_addr,create_time from t_card_data " +
            "where 1=1 " +
            "<if test=\"macAddr != null and macAddr != ''\"> and  mac_addr = #{macAddr}</if>" +
            "order by mac_addr,card_version desc"+
            "</script>"})
    List<CardData> listCardData(@Param("macAddr") String macAddr);

    @Select("select card_version,card_data,mac_addr,create_time from t_card_data " +
            "where mac_addr = #{cardData.macAddr} and card_version=#{cardData.cardVersion}")
    CardData getCardData(@Param("cardData") CardData cardData);

}
