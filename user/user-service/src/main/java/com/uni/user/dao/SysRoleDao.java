package com.uni.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uni.user.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {
    /**
   * 查询列表
   */
    List<SysRoleEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);
}
