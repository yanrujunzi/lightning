package com.healthy.food.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FoodManageDto implements Serializable {

    /**
    * 食物id
    */
    private Long idFood;
    /**
     * 食物名称
     */
    private String name;
    /**
     * 卡路里 单位每克多少卡路里
     */
    private BigDecimal calorie;
    /**
     * 创建时间
     */
    private Date dateCreate;
}
