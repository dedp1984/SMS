package com.pujjr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.SmsWaitSend;

public interface SmsWaitSendMapper {
    int deleteByPrimaryKey(String id);

    int insert(SmsWaitSend record);

    int insertSelective(SmsWaitSend record);

    SmsWaitSend selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SmsWaitSend record);

    int updateByPrimaryKey(SmsWaitSend record);
    
    List<SmsWaitSend> selectList(@Param("procStatus")String procStatus);
}