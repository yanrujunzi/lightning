package com.uni.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SysUserDto implements Serializable {

    /**
    * 
    */
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

    /**
     * 登录id
     */
    private String loginId;
    /**
     * 加密后的密码
     */
    private String encryptPassword;
    /**
     * 验证码唯一标识
     */
    private String captchaId;
    /**
     * 验证码
     */
    private String captcha;

    /**
    * 0锁定1正常
    */
    private Byte status;

    private Date editTime;

    private Date createTime;

    private Long groupId;

    private List<SysRoleDto> userRoleList = new ArrayList<>();

    /**
     * 角色
     */
    private String roleName;

    private int version;

}
