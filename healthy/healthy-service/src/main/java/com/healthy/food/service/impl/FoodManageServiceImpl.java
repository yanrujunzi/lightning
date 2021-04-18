package com.healthy.food.service.impl;

import com.healthy.food.dao.FoodManageDao;
import com.healthy.food.dto.FoodManageDto;
import com.healthy.food.entity.FoodManageEntity;
import com.healthy.food.service.FoodManageService;
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
public class FoodManageServiceImpl implements FoodManageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodManageServiceImpl.class);

    @Autowired
    private FoodManageDao foodManageDao;


    @Override
    public List<FoodManageDto> queryList(Map<String,Object> params){
        List<FoodManageEntity> list = foodManageDao.queryList(params);
        return CopyUtils.copyList(list,FoodManageDto.class);
    }

    @Override
    public int queryTotal(Map<String, Object> params){
        return foodManageDao.queryTotal(params);
    }

    @Override
    public Long save(FoodManageDto dto){
        FoodManageEntity entity = CopyUtils.copyObj(dto,FoodManageEntity.class);
        entity.setIdFood(SnowFlake.nextId());
        foodManageDao.insert(entity);
        return entity.getIdFood();
    }

    @Override
    public void updateById(FoodManageDto dto){
        FoodManageEntity entity = CopyUtils.copyObj(dto,FoodManageEntity.class);
        foodManageDao.updateById(entity);
    }

    @Override
    public FoodManageDto selectById(Serializable id){
        FoodManageEntity entity = foodManageDao.selectById(id);
        return CopyUtils.copyObj(entity,FoodManageDto.class);
    }

    @Override
    public void deleteById(Serializable id){
        Map<String,Object> param=new HashMap<>();
        param.put("idFood",id);
        foodManageDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(Serializable[] ids){
        for (Serializable id : ids) {
            deleteById(id);
        }
    }


}
