package com.uni.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class SysMenuDto implements Serializable {

    /**
    * 
    */
    private Long id;
    /**
    * 
    */
    private Long parentId;
    /**
    * 
    */
    private String name;
    /**
    * 
    */
    private String icon;
    /**
    * url
    */
    private String url;
    /**
    * 菜单等级1顶级2子菜单3按钮
    */
    private Byte level;
    /**
    * 权限名称 如sysUser:edit
    */
    private String permission;
    /**
    * 0禁用1正常
    */
    private Byte status;
    /**
    * 排序每一个菜单下都是从0开始
    */
    private Integer sort;
    /**
    * 
    */
    private Date editTime;
    /**
    * 
    */
    private Date createTime;

    /**
     * 节点是否选中 默认不选中 角色勾选后默认选中
     */
    private boolean checked = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysMenuDto that = (SysMenuDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
