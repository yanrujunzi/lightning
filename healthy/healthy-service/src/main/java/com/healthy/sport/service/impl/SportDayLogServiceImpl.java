package com.healthy.sport.service.impl;

import com.healthy.sport.dao.SportDayLogDao;
import com.healthy.sport.dto.SportDayLogDto;
import com.healthy.sport.entity.SportDayLogEntity;
import com.healthy.sport.service.SportDayLogService;
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
public class SportDayLogServiceImpl implements SportDayLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportDayLogServiceImpl.class);

    @Autowired
    private SportDayLogDao sportDayLogDao;


    @Override
    public List<SportDayLogDto> queryList(Map<String,Object> params){
        List<SportDayLogEntity> list = sportDayLogDao.queryList(params);
        return CopyUtils.copyList(list,SportDayLogDto.class);
    }

    @Override
    public int queryTotal(Map<String, Object> params){
        return sportDayLogDao.queryTotal(params);
    }

    @Override
    public Long save(SportDayLogDto dto){
        SportDayLogEntity entity = CopyUtils.copyObj(dto,SportDayLogEntity.class);
        entity.setIdSport(SnowFlake.nextId());
        sportDayLogDao.insert(entity);
        return entity.getIdSport();
    }

    @Override
    public void updateById(SportDayLogDto dto){
        SportDayLogEntity entity = CopyUtils.copyObj(dto,SportDayLogEntity.class);
        sportDayLogDao.updateById(entity);
    }

    @Override
    public SportDayLogDto selectById(Serializable id){
        SportDayLogEntity entity = sportDayLogDao.selectById(id);
        return CopyUtils.copyObj(entity,SportDayLogDto.class);
    }

    @Override
    public void deleteById(Serializable id){
        Map<String,Object> param=new HashMap<>();
        param.put("idSport",id);
        sportDayLogDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(Serializable[] ids){
        for (Serializable id : ids) {
            deleteById(id);
        }
    }


}
