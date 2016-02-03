package com.pujjr.dao;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.TplDtl;
import com.pujjr.domain.TplDtlKey;

public interface TplDtlMapper {

	int deleteByPrimaryKey(TplDtlKey key);

    int insert(TplDtl record);

    int insertSelective(TplDtl record);

    TplDtl selectByPrimaryKey(TplDtlKey key);

    int updateByPrimaryKeySelective(TplDtl record);

    int updateByPrimaryKey(TplDtl record);
    
    int deleteByTemplateId(@Param("id")String id);
}