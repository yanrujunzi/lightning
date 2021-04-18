package com.healthy.body.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("body_manage")
public class BodyManageEntity {

    /**
    * 身体id
    */
    @TableId
    private Long idBody;
    /**
     * 用户名称，爱填不填
     */
    private String name;
    /**
     * 所属用户id
     */
    private Long idUser;
    /**
     * 与本人关系 01 本人 02 子女 03 父母 04 配偶 05 好盆友
     */
    private String relationship;
    /**
     * 身高 单位 cm
     */
    private String height;
    /**
     * 出生日期 精确到天
     */
    private String birthdDate;
    /**
     * 期望体重，单位kg 精确到小数点后两位
     */
    private BigDecimal expectedWeight;
    /**
     * 创建时间
     */
    private Date dateCreate;
}
