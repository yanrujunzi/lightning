package com.uni.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uni.user.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {
    /**
   * 查询列表
   */
    List<SysMenuEntity> queryList();

    Integer queryMaxSortByPid(Long parentId);

    void clearByModuleId(Long moduleId);
}
