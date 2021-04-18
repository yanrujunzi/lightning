package com.healthy.body.service.impl;

import com.healthy.body.dao.BodyManageDao;
import com.healthy.body.dto.BodyManageDto;
import com.healthy.body.entity.BodyManageEntity;
import com.healthy.body.service.BodyManageService;
import com.uni.util.CopyUtils;
import com.uni.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BodyManageServiceImpl implements BodyManageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BodyManageServiceImpl.class);

    @Autowired
    private BodyManageDao bodyManageDao;


    @Override
    public List<BodyManageDto> queryList(Map<String,Object> params){
        List<BodyManageEntity> list = bodyManageDao.queryList(params);
        return CopyUtils.copyList(list,BodyManageDto.class);
    }

    @Override
    public int queryTotal(Map<String, Object> params){
        return bodyManageDao.queryTotal(params);
    }

    @Override
    public Long save(BodyManageDto dto){
        BodyManageEntity entity = CopyUtils.copyObj(dto,BodyManageEntity.class);
        entity.setIdBody(SnowFlake.nextId());
        bodyManageDao.insert(entity);
        return entity.getIdBody();
    }

    @Override
    public void updateById(BodyManageDto dto){
        BodyManageEntity entity = CopyUtils.copyObj(dto,BodyManageEntity.class);
        bodyManageDao.updateById(entity);
    }

    @Override
    public BodyManageDto selectById(Serializable id){
        BodyManageEntity entity = bodyManageDao.selectById(id);
        return CopyUtils.copyObj(entity,BodyManageDto.class);
    }

    @Override
    public void deleteById(Serializable id){
        Map<String,Object> param=new HashMap<>();
        param.put("idBody",id);
        bodyManageDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(Serializable[] ids){
        for (Serializable id : ids) {
            deleteById(id);
        }
    }


}
