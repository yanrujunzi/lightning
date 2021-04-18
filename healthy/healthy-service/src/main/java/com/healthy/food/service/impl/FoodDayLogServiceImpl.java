package com.healthy.food.service.impl;

import com.healthy.food.dao.FoodDayLogDao;
import com.healthy.food.dto.FoodDayLogDto;
import com.healthy.food.entity.FoodDayLogEntity;
import com.healthy.food.service.FoodDayLogService;
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
public class FoodDayLogServiceImpl implements FoodDayLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodDayLogServiceImpl.class);

    @Autowired
    private FoodDayLogDao foodDayLogDao;


    @Override
    public List<FoodDayLogDto> queryList(Map<String,Object> params){
        List<FoodDayLogEntity> list = foodDayLogDao.queryList(params);
        return CopyUtils.copyList(list,FoodDayLogDto.class);
    }

    @Override
    public int queryTotal(Map<String, Object> params){
        return foodDayLogDao.queryTotal(params);
    }

    @Override
    public Long save(FoodDayLogDto dto){
        FoodDayLogEntity entity = CopyUtils.copyObj(dto,FoodDayLogEntity.class);
        entity.setIdFoodLog(SnowFlake.nextId());
        foodDayLogDao.insert(entity);
        return entity.getIdFoodLog();
    }

    @Override
    public void updateById(FoodDayLogDto dto){
        FoodDayLogEntity entity = CopyUtils.copyObj(dto,FoodDayLogEntity.class);
        foodDayLogDao.updateById(entity);
    }

    @Override
    public FoodDayLogDto selectById(Serializable id){
        FoodDayLogEntity entity = foodDayLogDao.selectById(id);
        return CopyUtils.copyObj(entity,FoodDayLogDto.class);
    }

    @Override
    public void deleteById(Serializable id){
        Map<String,Object> param=new HashMap<>();
        param.put("idFoodLog",id);
        foodDayLogDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(Serializable[] ids){
        for (Serializable id : ids) {
            deleteById(id);
        }
    }


}
