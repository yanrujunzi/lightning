package com.healthy.body.service;


import com.healthy.body.dto.BodyManageDto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BodyManageService {

    /**
    * 查询列表
    */
    List<BodyManageDto> queryList(Map<String, Object> params);

    /**
   * 查询数量
   */
    int queryTotal(Map<String, Object> map);

    /**
    * 存入一条记录
    * @param dto
    * @return 存入数据的id
    */
    Long save(BodyManageDto dto);

    /**
    * 根据id更新一条记录
    * @param dto
    */
    void updateById(BodyManageDto dto);

    /**
    * 根据id查询一条记录
    * @param id
    */
    BodyManageDto selectById(Serializable id);

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



}
