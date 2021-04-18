package com.uni.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uni.user.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {
    /**
   * 查询列表
   */
    List<SysUserRoleEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);

    List<Long> selectRoleIdsByUserId(Serializable userId);
}
