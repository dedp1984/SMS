package com.pujjr.dao;

import com.pujjr.domain.AJB;

public interface AJBMapper {
    int deleteByPrimaryKey(String id);

    int insert(AJB record);

    int insertSelective(AJB record);

    AJB selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AJB record);

    int updateByPrimaryKey(AJB record);
}