package com.healthy.food.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FoodDayLogDto implements Serializable {

    /**
    * 每日饮食日志
    */
    private Long idFoodLog;
    /**
    * 人体id
    */
    private Long idBody;
    /**
    * 所属用户id
    */
    private Long idUser;
    /**
    * 食物id
    */
    private Long idFood;
    /**
    * 人员名称
    */
    private String bodyName;
    /**
    * 食物名称
    */
    private String foodName;
    /**
     * 食用重量 单位 克 精确到小数点后1位
     */
    private BigDecimal weight;
    /**
     * 总热量 卡路里 精确到小数点后一位
     */
    private BigDecimal calorie;
    /**
     * 进食日期精确到天
     */
    private String grubTime;
    /**
     * 创建时间
     */
    private Date dateCreate;
}
