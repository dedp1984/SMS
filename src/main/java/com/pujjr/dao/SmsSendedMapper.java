package com.pujjr.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.SmsSended;

public interface SmsSendedMapper {

	int deleteByPrimaryKey(String id);

    int insert(SmsSended record);

    int insertSelective(SmsSended record);

    SmsSended selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SmsSended record);

    int updateByPrimaryKey(SmsSended record);
    
    List<SmsSended> selectList(Map map);
    
    SmsSended selectBySendTaskId(@Param("sendtaskid")String sendTaskId,
    		                    @Param("tel")String tel);
    
    int updateBySendTaskId(SmsSended record);
}