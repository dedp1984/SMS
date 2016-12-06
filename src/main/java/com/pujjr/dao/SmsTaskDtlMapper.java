package com.pujjr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.SmsTaskDtl;

public interface SmsTaskDtlMapper {
    int deleteByPrimaryKey(String id);

    int insert(SmsTaskDtl record);

    int insertSelective(SmsTaskDtl record);

    SmsTaskDtl selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SmsTaskDtl record);

    int updateByPrimaryKey(SmsTaskDtl record);
    
    List<SmsTaskDtl> selectListByTaskId(@Param("taskid")String taskid,
    		                            @Param("procStatus")String procStatus);
    
    int deleteByTaskId(@Param("taskid")String taskid);
=======
package com.pujjr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.SmsTaskDtl;

public interface SmsTaskDtlMapper {
    int deleteByPrimaryKey(String id);

    int insert(SmsTaskDtl record);

    int insertSelective(SmsTaskDtl record);

    SmsTaskDtl selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SmsTaskDtl record);

    int updateByPrimaryKey(SmsTaskDtl record);
    
    List<SmsTaskDtl> selectListByTaskId(@Param("taskid")String taskid);
    
    int deleteByTaskId(@Param("taskid")String taskid);
}