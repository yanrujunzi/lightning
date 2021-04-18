package com.healthy.sport.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthy.sport.entity.SportDayLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SportDayLogDao extends BaseMapper<SportDayLogEntity> {
    /**
   * 查询列表
   */
    List<SportDayLogEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);

}
