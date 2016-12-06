package com.pujjr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.JXS;

public interface JXSMapper {
    int deleteByPrimaryKey(String id);

    int insert(JXS record);

    int insertSelective(JXS record);

    JXS selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JXS record);

    int updateByPrimaryKey(JXS record);
    
    List<JXS> selectList(@Param("name")String name);
}