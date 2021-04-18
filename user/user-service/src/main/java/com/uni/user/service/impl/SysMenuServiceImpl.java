package com.uni.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.uni.user.dao.SysMenuDao;
import com.uni.user.dao.SysRoleMenuDao;
import com.uni.user.dto.SysMenuDto;
import com.uni.user.entity.SysMenuEntity;
import com.uni.user.entity.SysRoleMenuEntity;
import com.uni.user.service.SysMenuService;
import com.uni.util.CopyUtils;
import com.uni.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;


    @Override
    public List<SysMenuDto> queryList(){
        List<SysMenuEntity> list = sysMenuDao.queryList();
        return CopyUtils.copyList(list, SysMenuDto.class);
    }


    @Override
    public void save(SysMenuDto dto){
        SysMenuEntity entity = CopyUtils.copyObj(dto, SysMenuEntity.class);
        entity.setId(SnowFlake.nextId());
        sysMenuDao.insert(entity);
    }

    @Override
    public void updateById(SysMenuDto dto){
        SysMenuEntity entity = CopyUtils.copyObj(dto, SysMenuEntity.class);
        sysMenuDao.updateById(entity);
    }

    @Override
    public SysMenuDto selectById(Serializable id){
        SysMenuEntity entity = sysMenuDao.selectById(id);
        return CopyUtils.copyObj(entity, SysMenuDto.class);
    }

    @Override
    public void deleteById(Serializable id){
        sysMenuDao.deleteById(id);
    }

    @Override
    @Transactional
    public String deleteBatch(Serializable[] ids){
        Map<String,Object> map=new HashMap<>();
        for (Serializable id : ids) {
            map.put("parent_id",id);
            List<SysMenuEntity> sysMenuEntities=  this.sysMenuDao.selectByMap(map);
            if(sysMenuEntities!=null && sysMenuEntities.size()>0 ){
                return "请先删除子目录";
            }
        }
        for (Serializable id : ids) {
            //删除角色与菜单的关系
            sysRoleMenuDao.delete(new QueryWrapper<SysRoleMenuEntity>().eq("menu_id", id));
            //删除菜单
            deleteById(id);
        }
        return null;
    }

    @Override
    public List<SysMenuDto> queryCanUsefulMenus() {
        List<SysMenuEntity> entityList = sysMenuDao.selectList(new QueryWrapper<SysMenuEntity>().eq("status", 1).orderByAsc("sort"));
        return CopyUtils.copyList(entityList, SysMenuDto.class);
    }

    @Override
    public List<SysMenuDto> queryByParentId(Long parentId) {
        List<SysMenuEntity> entityList = sysMenuDao.selectList(new QueryWrapper<SysMenuEntity>().eq("status", 1).eq("parent_id", parentId).orderByAsc("sort"));
        return CopyUtils.copyList(entityList, SysMenuDto.class);
    }

    @Override
    public List<SysMenuDto> selectModuleList() {
        List<SysMenuEntity> sysMenuEntities = sysMenuDao.selectList(new QueryWrapper<SysMenuEntity>().eq("level", 0).eq("status", 1).orderByAsc("sort"));
        return CopyUtils.copyList(sysMenuEntities, SysMenuDto.class);
    }

    @Override
    public void clearByModuleId(Long moduleId) {
        sysMenuDao.clearByModuleId(moduleId);
    }

}
