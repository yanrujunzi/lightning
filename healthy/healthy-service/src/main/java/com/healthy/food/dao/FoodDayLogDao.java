package com.healthy.food.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthy.food.entity.FoodDayLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FoodDayLogDao extends BaseMapper<FoodDayLogEntity> {
    /**
   * 查询列表
   */
    List<FoodDayLogEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);

}
