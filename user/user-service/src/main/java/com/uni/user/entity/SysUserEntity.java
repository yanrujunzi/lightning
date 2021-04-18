package com.uni.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_user")
public class SysUserEntity {

    /**
    * 
    */
    @TableId
    private Long id;
    /**
    * 
    */
    private String account;
    /**
    * 
    */
    private String username;
    /**
    * 
    */
    private String password;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * email
     */
    private String email;

    private Date editTime;

    private Date createTime;

    /**
    * 0锁定1正常
    */
    private Byte status;

    private Long groupId;

    private int version;


}
