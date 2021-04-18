package com.uni.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uni.user.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
    /**
   * 查询列表
   */
    List<SysUserEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);
}
