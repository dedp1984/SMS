package com.pujjr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pujjr.domain.Template;

public interface TemplateMapper {

	int deleteByPrimaryKey(String id);

    int insert(Template record);

    int insertSelective(Template record);

    Template selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Template record);

    int updateByPrimaryKey(Template record);
    
    List<Template> selectList(@Param("name")String name);
    
}