package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.CardData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CardDataMapper {

    int addCardData(@Param("cardData") CardData cardData);

    List<CardData> listCardData(@Param("macAddr") String macAddr);

    CardData getCardData(@Param("cardData") CardData cardData);

}
