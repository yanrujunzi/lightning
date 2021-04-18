package com.uni.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class SysRoleMenuEntity {

    /**
    * 
    */
    @TableId
    private Long id;
    /**
    * 
    */
    private Long roleId;
    /**
    * 
    */
    private Long menuId;

}
