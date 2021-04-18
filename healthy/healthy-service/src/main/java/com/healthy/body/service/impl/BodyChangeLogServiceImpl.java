package com.healthy.body.service.impl;

import com.healthy.body.dao.BodyChangeLogDao;
import com.healthy.body.dto.BodyChangeLogDto;
import com.healthy.body.entity.BodyChangeLogEntity;
import com.healthy.body.service.BodyChangeLogService;
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
public class BodyChangeLogServiceImpl implements BodyChangeLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BodyChangeLogServiceImpl.class);

    @Autowired
    private BodyChangeLogDao bodyChangeLogDao;


    @Override
    public List<BodyChangeLogDto> queryList(Map<String,Object> params){
        List<BodyChangeLogEntity> list = bodyChangeLogDao.queryList(params);
        return CopyUtils.copyList(list,BodyChangeLogDto.class);
    }

    @Override
    public int queryTotal(Map<String, Object> params){
        return bodyChangeLogDao.queryTotal(params);
    }

    @Override
    public Long save(BodyChangeLogDto dto){
        BodyChangeLogEntity entity = CopyUtils.copyObj(dto,BodyChangeLogEntity.class);
        entity.setIdBodyLog(SnowFlake.nextId());
        bodyChangeLogDao.insert(entity);
        return entity.getIdBodyLog();
    }

    @Override
    public void updateById(BodyChangeLogDto dto){
        BodyChangeLogEntity entity = CopyUtils.copyObj(dto,BodyChangeLogEntity.class);
        bodyChangeLogDao.updateById(entity);
    }

    @Override
    public BodyChangeLogDto selectById(Serializable id){
        BodyChangeLogEntity entity = bodyChangeLogDao.selectById(id);
        return CopyUtils.copyObj(entity,BodyChangeLogDto.class);
    }

    @Override
    public void deleteById(Serializable id){
        Map<String,Object> param=new HashMap<>();
        param.put("idBodyLog",id);
        bodyChangeLogDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(Serializable[] ids){
        for (Serializable id : ids) {
            deleteById(id);
        }
    }



}
