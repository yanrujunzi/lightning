package com.uni.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.uni.user.dao.SysRoleDao;
import com.uni.user.dao.SysRoleMenuDao;
import com.uni.user.dto.SysMenuDto;
import com.uni.user.dto.SysRoleDto;
import com.uni.user.entity.SysMenuEntity;
import com.uni.user.entity.SysRoleEntity;
import com.uni.user.entity.SysRoleMenuEntity;
import com.uni.user.service.SysMenuService;
import com.uni.user.service.SysRoleService;
import com.uni.util.CopyUtils;
import com.uni.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public SysRoleDto selectByRoleName(String roleName) {

        SysRoleEntity sysRoleEntity = sysRoleDao.selectOne(new QueryWrapper<SysRoleEntity>().eq("role_name", roleName).last("limit 1"));
        return CopyUtils.copyObj(sysRoleEntity, SysRoleDto.class);
    }

    @Override
    public List<SysRoleDto> queryList(Map<String,Object> params){
        List<SysRoleEntity> list = sysRoleDao.queryList(params);
        return CopyUtils.copyList(list, SysRoleDto.class);
    }

    @Override
    public int queryTotal(Map<String, Object> params){
        return sysRoleDao.queryTotal(params);
    }

    @Override
    @Transactional
    public void save(SysRoleDto dto){
        Date now = new Date();

        dto.setId(SnowFlake.nextId());
        dto.setStatus((byte) 1);

        SysRoleEntity entity = CopyUtils.copyObj(dto,SysRoleEntity.class);
        sysRoleDao.insert(entity);

        saveRoleMenuRelation(dto);
    }

    @Override
    @Transactional
    public void updateById(SysRoleDto dto){
        SysRoleEntity entity = CopyUtils.copyObj(dto,SysRoleEntity.class);
        sysRoleDao.updateById(entity);
        saveRoleMenuRelation(dto);
    }



    @Override
    public SysRoleDto selectById(Serializable id){
        //用户菜单信息
        List<SysMenuDto> roleMenuList = queryRoleMenus(id);
        SysRoleDto sysRoleDto = CopyUtils.copyObj(sysRoleDao.selectById(id), SysRoleDto.class);
        if (sysRoleDto != null) {
            sysRoleDto.setRoleMenuList(roleMenuList);
        }
        return sysRoleDto;
    }

    @Override
    public SysRoleDto selectById(Serializable id, Long moduleId) {
        //用户菜单信息
        List<SysMenuDto> roleMenuList = queryRoleMenus(id, moduleId);
        SysRoleDto sysRoleDto = CopyUtils.copyObj(sysRoleDao.selectById(id), SysRoleDto.class);
        if (sysRoleDto != null) {
            sysRoleDto.setRoleMenuList(roleMenuList);
        }
        return sysRoleDto;
    }

    @Override
    public SysRoleDto selectZeroMenuById(Serializable id){
        List<SysMenuDto> result = new ArrayList<>();

        List<SysMenuDto> canUsefulMenus = sysMenuService.selectModuleList();
        List<SysMenuEntity> roleMenuList = sysRoleMenuDao.selectByRoleId(id);
        if (!CollectionUtils.isEmpty(canUsefulMenus)) {
            for (SysMenuDto canUsefulMenu : canUsefulMenus) {
                SysMenuDto item = CopyUtils.copyObj(canUsefulMenu, SysMenuDto.class);

                if (!CollectionUtils.isEmpty(roleMenuList)) {
                    for (SysMenuEntity sysMenuEntity : roleMenuList) {
                        if (Objects.equals(canUsefulMenu.getId(), sysMenuEntity.getId())) {
                            item.setChecked(true);
                        }
                    }
                }
                result.add(item);
            }
        }
        SysRoleDto sysRoleDto = CopyUtils.copyObj(sysRoleDao.selectById(id), SysRoleDto.class);
        sysRoleDto.setRoleMenuList(result);
        return sysRoleDto;
    }

    @Override
    @Transactional
    public void deleteById(Serializable id){
        sysRoleDao.deleteById(id);
        //删除角色与菜单的关系
        sysRoleMenuDao.delete(new QueryWrapper<SysRoleMenuEntity>().eq("role_id", id));
    }

    @Override
    @Transactional
    public void deleteBatch(Serializable[] ids){
        for (Serializable id : ids) {
            deleteById(id);
        }
    }

    @Override
    public List<SysRoleDto> queryAllRoles() {
        List<SysRoleEntity> sysRoleEntityList = sysRoleDao.selectList(new QueryWrapper<SysRoleEntity>().eq("status", 1));
        return CopyUtils.copyList(sysRoleEntityList, SysRoleDto.class);
    }

    @Override
    public List<SysMenuDto> queryRoleMenus(Serializable roleId) {
        List<SysMenuDto> result = new ArrayList<>();

        //得到所有启用的菜单
        List<SysMenuDto> canUsefulMenus = sysMenuService.queryCanUsefulMenus();
        List<SysMenuEntity> roleMenuList = sysRoleMenuDao.selectByRoleId(roleId);

        if (!CollectionUtils.isEmpty(canUsefulMenus)) {
            for (SysMenuDto canUsefulMenu : canUsefulMenus) {

                SysMenuDto item = CopyUtils.copyObj(canUsefulMenu, SysMenuDto.class);

                if (!CollectionUtils.isEmpty(roleMenuList)) {
                    for (SysMenuEntity roleMenuEntity : roleMenuList) {
                        if (Objects.equals(canUsefulMenu.getId(), roleMenuEntity.getId())) {
                            //说明我拥有菜单树上的菜单
                            item.setChecked(true);
                        }
                    }
                }
                result.add(item);
            }
        }
        return result;
    }

    private List<SysMenuDto> queryRoleMenus(Serializable roleId, Long moduleId) {
        List<SysMenuDto> result = new ArrayList<>();

        // level为1 模块下级菜单等级 (根目录-1 模块0 目录1 菜单2 按钮3) moduleId为11系统应用
        List<SysMenuDto> canUsefulMenus = this.queryCanUsefulMenus(11L, 1);
        canUsefulMenus.addAll(this.queryCanUsefulMenus(moduleId,1));
        List<SysMenuEntity> roleMenuList = sysRoleMenuDao.selectByRoleId(roleId);

        if (!CollectionUtils.isEmpty(canUsefulMenus)) {
            for (SysMenuDto canUsefulMenu : canUsefulMenus) {

                SysMenuDto item = CopyUtils.copyObj(canUsefulMenu, SysMenuDto.class);

                if (!CollectionUtils.isEmpty(roleMenuList)) {
                    for (SysMenuEntity roleMenuEntity : roleMenuList) {
                        if (Objects.equals(canUsefulMenu.getId(), roleMenuEntity.getId())) {
                            //说明我拥有菜单树上的菜单
                            item.setChecked(true);
                        }
                    }
                }
                result.add(item);
            }
        }
        return result;
    }

    private List<SysMenuDto> queryCanUsefulMenus(Long moduleId, int level){
        List<SysMenuDto> list = new ArrayList<>();
        if (level == 3){
            return sysMenuService.queryByParentId(moduleId);
        }
        List<SysMenuDto> sysMenuDtos = sysMenuService.queryByParentId(moduleId);

        list.addAll(sysMenuDtos);
        for (SysMenuDto sysMenuDto : sysMenuDtos) {
            list.addAll(queryCanUsefulMenus(sysMenuDto.getId(), level+1));
        }
        return list;
    }

    /**
     * 保存角色和菜单的关系
     * @param dto
     */
    private void saveRoleMenuRelation(SysRoleDto dto) {
        //第一步先清除关系
        sysRoleMenuDao.delete(new QueryWrapper<SysRoleMenuEntity>().eq("role_id", dto.getId()));
        if (CollectionUtils.isEmpty(dto.getRoleMenuList())) {
            return;
        }
        for (SysMenuDto sysMenuDto : dto.getRoleMenuList()) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setId(SnowFlake.nextId());
            sysRoleMenuEntity.setRoleId(dto.getId());
            sysRoleMenuEntity.setMenuId(sysMenuDto.getId());
            sysRoleMenuDao.insert(sysRoleMenuEntity);
        }
    }
}
