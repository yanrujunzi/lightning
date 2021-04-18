package com.uni.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SysRoleDto implements Serializable {

    /**
    * ID
    */
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

    private List<SysMenuDto> roleMenuList = new ArrayList<>();

}
