package com.uni.user.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.uni.user.dto.SysRoleDto;
import com.uni.user.dto.SysMenuDto;

public interface SysRoleService {
    /**
     * 根据角色英文名查询
     * @param roleName
     * @return
     */
    SysRoleDto selectByRoleName(String roleName);

    /**
    * 查询列表
    */
    List<SysRoleDto> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> map);

    /**
    * 存入一条记录
    * @param dto
    */
    void save(SysRoleDto dto);

    /**
    * 根据id更新一条记录
    * @param dto
    */
    void updateById(SysRoleDto dto);

    /**
    * 根据id查询一条记录
    * @param id
    */
    SysRoleDto selectById(Serializable id);

    /**
     * 根据id、模块id查询一条记录
     * @param id
     */
    SysRoleDto selectById(Serializable id, Long moduleId);

    SysRoleDto selectZeroMenuById(Serializable id);

    /**
    * 根据id删除一条记录
    * @param id
    */
    void deleteById(Serializable id);

    /**
   * 批量删除
   * @param ids
   */
    void deleteBatch(Serializable[] ids);

    List<SysRoleDto> queryAllRoles();

    /**
     * 查询角色拥有的权限（只查询未删除角色的菜单）
     * @param roleId
     * @return
     */
    List<SysMenuDto> queryRoleMenus(Serializable roleId);


}
