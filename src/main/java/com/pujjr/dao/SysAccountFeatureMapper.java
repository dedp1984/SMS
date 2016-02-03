package com.pujjr.dao;

import java.util.List;

import com.pujjr.domain.SysAccountFeatureKey;

public interface SysAccountFeatureMapper {
    int deleteByPrimaryKey(SysAccountFeatureKey key);

    int insert(SysAccountFeatureKey record);

    int insertSelective(SysAccountFeatureKey record);
    
    int deleteBySelective(SysAccountFeatureKey record);
    
    List<SysAccountFeatureKey> selectBySelective(SysAccountFeatureKey record);
}