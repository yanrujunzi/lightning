package com.healthy.config;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.healthy.constants.Constant;
import com.healthy.shiro.jwt.JwtToken;
import com.healthy.util.JwtUtil;
import com.healthy.util.RedisUtil;
import com.uni.user.dto.SysMenuDto;
import com.uni.user.dto.SysRoleDto;
import com.uni.user.dto.SysUserDto;
import com.uni.user.service.SysMenuService;
import com.uni.user.service.SysRoleService;
import com.uni.user.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 自定义Realm
 */
@Service
public class UserRealm extends AuthorizingRealm {

    @Reference
    private SysUserService sysUserService;
    @Reference
    private SysRoleService sysRoleService;
    @Reference
    private SysMenuService sysMenuService;


    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String account = JwtUtil.getClaim(principalCollection.toString(), Constant.ACCOUNT);
        SysUserDto sysUserDto = sysUserService.selectByAccount(account);
        // 查询用户角色
        List<SysRoleDto> sysRoleDtos = sysUserService.selectById(sysUserDto.getId()).getUserRoleList();
        for (SysRoleDto sysRoleDto : sysRoleDtos) {
            if (sysRoleDto != null) {
                // 添加角色
                simpleAuthorizationInfo.addRole(sysRoleDto.getRoleName());
                // 根据用户角色查询权限
                List<SysMenuDto> sysMenuDtos = sysRoleService.selectById(sysRoleDto.getId()).getRoleMenuList();
                for (SysMenuDto sysMenuDto : sysMenuDtos) {
                    if (sysMenuDto != null) {
                        // 添加权限
                        if(sysMenuDto.getPermission()!=null){
                            simpleAuthorizationInfo.addStringPermission(sysMenuDto.getPermission());
                        }
                    }
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 帐号为空
        if (StringUtils.isBlank(account)) {
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
        }
        // 查询用户是否存在
        SysUserDto sysUserDto = sysUserService.selectByAccount(account);
        if (sysUserDto == null) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token) && RedisUtil.hasKey(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = RedisUtil.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }
}
