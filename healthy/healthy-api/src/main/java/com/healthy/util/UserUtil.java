package com.healthy.util;

import com.alibaba.dubbo.config.annotation.Reference;
import com.uni.user.dto.SysRoleDto;
import com.uni.user.dto.SysUserDto;
import com.uni.user.service.SysRoleService;
import com.uni.user.service.SysUserService;
import com.healthy.exception.CustomException;
import com.healthy.constants.Constant;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取当前登录用户工具类
 *
 */
@Component
public class UserUtil {

    @Reference
    private SysUserService sysUserService;
    @Reference
    private SysRoleService sysRoleService;


    /**
     * 获取当前登录用户
     */
    public SysUserDto getUser() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        // 解密获得Account
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        SysUserDto sysUserDto = sysUserService.selectByAccount(account);
        // 用户是否存在
        if (sysUserDto == null) {
            throw new CustomException("该帐号不存在(The account does not exist.)");
        }
        List<SysRoleDto> sysRoleDtos = sysUserService.selectById(sysUserDto.getId()).getUserRoleList();
        sysUserDto.setUserRoleList(sysRoleDtos);
        return sysUserDto;
    }

    /**
     * 获取当前登录用户Id
     */
    public Long getUserId() {
        return getUser().getId();
    }

    /**
     * 获取当前登录用户Token
     */
    public String getToken() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }

    /**
     * 获取当前登录用户Account
     *
     */
    public String getAccount() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        // 解密获得Account
        return JwtUtil.getClaim(token, Constant.ACCOUNT);
    }
}
