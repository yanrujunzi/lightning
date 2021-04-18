package com.healthy.body.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BodyChangeLogDto implements Serializable {

    /**
    * 身体变化日志id
    */
    private Long idBodyLog;
    /**
    * 身体id
    */
    private Long idBody;
    /**
     * 用户id
     */
    private Long idUser;
    /**
     * 人员姓名，爱填不填
     */
    private String name;
    /**
     * 与本人关系 01 本人 02 子女 03 父母 04 配偶 05 好盆友
     */
    private String relationship;
    /**
     * 身高 单位 cm
     */
    private String height;
    /**
     * 实时体重，单位kg 精确到小数点后两位
     */
    private BigDecimal weight;
    /**
     * 水份含量 精确到小数点后一位
     */
    private BigDecimal waterContent;
    /**
     * 体脂 单位 精确到小数点后一位
     */
    private BigDecimal bodyFat;
    /**
     * 肌肉 单位 kg， 精确到小数点后一位
     */
    private BigDecimal muscle;
    /**
     * 蛋白质含量，精确到小数点后一位
     */
    private BigDecimal protein;
    /**
     * BMI指标，精确到小数点后一位
     */
    private BigDecimal bmi;
    /**
     * 出生日期 精确到天
     */
    private String dateRecord;
    /**
     * 创建时间
     */
    private Date dateCreate;
}
