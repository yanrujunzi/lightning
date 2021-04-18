package com.uni.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.uni.user.dao.SysRoleDao;
import com.uni.user.dao.SysUserDao;
import com.uni.user.dao.SysUserRoleDao;
import com.uni.user.dto.SysRoleDto;
import com.uni.user.dto.SysUserDto;
import com.uni.user.entity.SysRoleEntity;
import com.uni.user.entity.SysUserEntity;
import com.uni.user.entity.SysUserRoleEntity;
import com.uni.user.service.SysUserService;
import com.uni.util.CopyUtils;
import com.uni.util.SnowFlake;
import com.uni.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;


    @Override
    public List<SysUserDto> queryList(Map<String,Object> params){
        List<SysUserEntity> list = sysUserDao.queryList(params);
        return CopyUtils.copyList(list, SysUserDto.class);
    }

    @Override
    public int queryTotal(Map<String, Object> params){
        return sysUserDao.queryTotal(params);
    }

    @Override
    @Transactional
    public void save(SysUserDto dto){


        dto.setId(SnowFlake.nextId());
        dto.setStatus((byte) 1);
        Date now = new Date();

        dto.setPassword(MD5.md5("123456"));

        saveUserRoleRelation(dto);


        SysUserEntity entity = CopyUtils.copyObj(dto, SysUserEntity.class);
        sysUserDao.insert(entity);
    }



    @Override
    @Transactional
    public void updateById(SysUserDto dto){
        SysUserEntity entity = CopyUtils.copyObj(dto, SysUserEntity.class);
        sysUserDao.updateById(entity);

        saveUserRoleRelation(dto);
    }

    @Override
    public SysUserDto selectById(Serializable id){
        SysUserEntity entity = sysUserDao.selectById(id);
        SysUserDto sysUserDto = CopyUtils.copyObj(entity, SysUserDto.class);
        if (sysUserDto != null) {
            //查询出没有被删除的角色
            List<Long> roleIds = sysUserRoleDao.selectRoleIdsByUserId(id);
            if (!CollectionUtils.isEmpty(roleIds)) {
                List<SysRoleDto> userRoleList = new ArrayList<>();
                for (Long roleId : roleIds) {
                    SysRoleEntity sysRoleEntity = sysRoleDao.selectById(roleId);
                    userRoleList.add(CopyUtils.copyObj(sysRoleEntity, SysRoleDto.class));
                }
                sysUserDto.setUserRoleList(userRoleList);
            }
        }
        return sysUserDto;
    }

    @Override
    @Transactional
    public void deleteById(Serializable id){
        sysUserDao.deleteById(id);
        //清除用户与角色的关系
        sysUserRoleDao.delete(new QueryWrapper<SysUserRoleEntity>().eq("user_id", id));
    }

    @Override
    @Transactional
    public void deleteBatch(Serializable[] ids){
        for (Serializable id : ids) {
            deleteById(id);
        }
    }

    @Override
    public SysUserDto selectByAccount(String account) {
        SysUserEntity entity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("account", account).last("limit 1"));
        return CopyUtils.copyObj(entity, SysUserDto.class);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setId(userId);
        sysUserEntity.setPassword(MD5.md5(password));
        sysUserDao.updateById(sysUserEntity);
    }

    private void saveUserRoleRelation(SysUserDto dto) {
        sysUserRoleDao.delete(new QueryWrapper<SysUserRoleEntity>().eq("user_id", dto.getId()));
        if (CollectionUtils.isEmpty(dto.getUserRoleList())) {
            return;
        }
        for (SysRoleDto sysRoleDto : dto.getUserRoleList()) {
            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
            sysUserRoleEntity.setId(SnowFlake.nextId());
            sysUserRoleEntity.setUserId(dto.getId());
            sysUserRoleEntity.setRoleId(sysRoleDto.getId());
            sysUserRoleDao.insert(sysUserRoleEntity);
        }
    }

	@Override
	public List<SysUserDto> selectListByGroupId(Serializable groupId) {
		List<SysUserEntity> sysUserEntitys = sysUserDao.selectList(new QueryWrapper<SysUserEntity>().eq("group_id", groupId));
		return CopyUtils.copyList(sysUserEntitys, SysUserDto.class);
	}
}
