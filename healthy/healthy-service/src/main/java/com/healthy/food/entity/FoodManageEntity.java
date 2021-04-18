package com.healthy.food.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("food_manage")
public class FoodManageEntity {

    /**
    * 食物id
    */
    @TableId
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
