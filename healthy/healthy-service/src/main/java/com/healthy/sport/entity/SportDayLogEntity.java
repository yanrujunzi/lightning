package com.healthy.sport.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sport_day_log")
public class SportDayLogEntity {

    /**
    * 运动id
    */
    @TableId
    private Long idSport;
    /**
     * 所属用户id
     */
    private Long idUser;
    /**
     * 人体id
     */
    private Long idBody;
    /**
     * 人员名称
     */
    private String bodyName;
    /**
     * 运动名称
     */
    private String name;
    /**
     * 持续时间，单位 分钟
     */
    private String duration;
    /**
     * 预计消耗能量 卡路里
     */
    private BigDecimal calorie;
    /**
     * 运动日期，精确到天
     */
    private String dateSport;
    /**
     * 创建时间
     */
    private Date dateCreate;
}
