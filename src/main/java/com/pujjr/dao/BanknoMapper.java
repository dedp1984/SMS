package com.pujjr.dao;

import com.pujjr.domain.Bankno;

public interface BanknoMapper {
    int deleteByPrimaryKey(String id);

    int insert(Bankno record);

    int insertSelective(Bankno record);

    Bankno selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Bankno record);

    int updateByPrimaryKey(Bankno record);
    
    Bankno selelctByBankname(String bankname);
}