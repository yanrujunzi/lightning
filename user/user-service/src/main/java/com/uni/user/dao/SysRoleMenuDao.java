package com.uni.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uni.user.entity.SysMenuEntity;
import com.uni.user.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {
    /**
   * 查询列表
   */
    List<SysRoleMenuEntity> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> params);

    List<SysMenuEntity> selectByRoleId(Serializable roleId);
}
