package com.uni.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_role")
public class SysRoleEntity {

    /**
    * ID
    */
    @TableId
    private Long id;
    /**
    * 角色中文名
    */
    private String roleCnName;
    /**
    * 角色英文名
    */
    private String roleName;
    /**
    * 状态1正常0锁定
    */
    private Byte status;
    /**
    * 
    */
    private Date createTime;
    /**
    * 
    */
    private Date editTime;

}
