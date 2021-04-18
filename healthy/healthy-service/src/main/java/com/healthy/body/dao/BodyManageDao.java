package com.healthy.body.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthy.body.entity.BodyManageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BodyManageDao extends BaseMapper<BodyManageEntity> {
    /**
   * 查询列表
   */
    List<BodyManageEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);

}
