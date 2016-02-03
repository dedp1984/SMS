package com.pujjr.dao;

import java.util.List;
import java.util.Map;

import com.pujjr.domain.SysAccount;

public interface SysAccountMapper {
    int deleteByPrimaryKey(String accountid);

    int insert(SysAccount record);

    int insertSelective(SysAccount record);

    SysAccount selectByPrimaryKey(String accountid);

    int updateByPrimaryKeySelective(SysAccount record);

    int updateByPrimaryKey(SysAccount record);
    
    List<SysAccount> selectAccountList(Map<String,Object> map);
    
    List<SysAccount>  selectAccountListByBranchId(String branchid);
}