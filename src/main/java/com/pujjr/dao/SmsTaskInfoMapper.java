package com.pujjr.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.SmsTaskInfo;

public interface SmsTaskInfoMapper {

    int insert(SmsTaskInfo record);

    int insertSelective(SmsTaskInfo record);

    SmsTaskInfo selectByPrimaryKey(String taskid);

    int updateByPrimaryKeySelective(SmsTaskInfo record);

    int updateByPrimaryKey(SmsTaskInfo record);
    
    List<SmsTaskInfo> selectList(@Param("taskname")String taskname,
    		                     @Param("starttime")Timestamp starttime,
    		                     @Param("endtime")Timestamp endtime,
    		                     @Param("createid")String createid);
    List<SmsTaskInfo> selectTimerTaskList(@Param("sendtime")Timestamp sendtime);
    
    int deleteByPrimaryKey(String taskid);
}