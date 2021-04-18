package com.uni.user.service;

import java.io.Serializable;
import java.util.List;

import com.uni.user.dto.SysMenuDto;

public interface SysMenuService {

    /**
    * 查询所有的菜单
    */
    List<SysMenuDto> queryList();


    /**
    * 存入一条记录
    * @param dto
    */
    void save(SysMenuDto dto);

    /**
    * 根据id更新一条记录
    * @param dto
    */
    void updateById(SysMenuDto dto);

    /**
    * 根据id查询一条记录
    * @param id
    */
    SysMenuDto selectById(Serializable id);

    /**
    * 根据id删除一条记录
    * @param id
    */
    void deleteById(Serializable id);

    /**
   * 批量删除
   * @param ids
   */
    String deleteBatch(Serializable[] ids);

    List<SysMenuDto> queryCanUsefulMenus();

    List<SysMenuDto> queryByParentId(Long parentId);

    List<SysMenuDto> selectModuleList();

    void clearByModuleId(Long moduleId);
}
