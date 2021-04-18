package com.healthy.body.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthy.body.entity.BodyChangeLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BodyChangeLogDao extends BaseMapper<BodyChangeLogEntity> {
    /**
   * 查询列表
   */
    List<BodyChangeLogEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);


}
