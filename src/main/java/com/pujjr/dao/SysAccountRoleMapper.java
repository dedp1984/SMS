package com.pujjr.dao;

import com.pujjr.domain.SysAccountRoleKey;

public interface SysAccountRoleMapper {
    int deleteByPrimaryKey(SysAccountRoleKey key);

    int insert(SysAccountRoleKey record);

    int insertSelective(SysAccountRoleKey record);
    
    int deleteByAccountid(String accountid);
}