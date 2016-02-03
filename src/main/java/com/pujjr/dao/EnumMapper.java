package com.pujjr.dao;

import com.pujjr.domain.Enum;
import com.pujjr.domain.EnumKey;

public interface EnumMapper {
    int deleteByPrimaryKey(EnumKey key);

    int insert(Enum record);

    int insertSelective(Enum record);

    Enum selectByPrimaryKey(EnumKey key);

    int updateByPrimaryKeySelective(Enum record);

    int updateByPrimaryKey(Enum record);
}