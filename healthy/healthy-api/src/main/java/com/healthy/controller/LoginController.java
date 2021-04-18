package com.healthy.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.healthy.constants.Constant;
import com.healthy.constants.RedisCacheConstants;
import com.healthy.util.UserUtil;
import com.healthy.util.JwtUtil;
import com.healthy.util.RedisUtil;
import com.uni.user.dto.SysMenuDto;
import com.uni.user.dto.SysRoleDto;
import com.uni.user.dto.SysUserDto;
import com.uni.user.service.SysRoleService;
import com.uni.user.service.SysUserService;
import com.uni.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


@RestController
@RequestMapping("sys")
@PropertySource("classpath:config.properties")
public class LoginController {

    /**
     * RefreshToken过期时间
     */
    @Value("${refreshTokenExpireTime}")
    private String refreshTokenExpireTime;
    @Reference
    private SysUserService sysUserService;
    @Reference
    private SysRoleService sysRoleService;
    @Autowired
    private UserUtil userUtil;

    /**
     * 前端获取公钥 然后把加密后的密码传入过来
     */
    @GetMapping("getPublicKey")
    public Response getPublicKey() {
        try {
            Map<String, Object> keyPair = RSAUtils.genKeyPair();
            String privateKey = RSAUtils.getPrivateKey(keyPair);
            String publicKey = RSAUtils.getPublicKey(keyPair);
            String loginId = UUIDUtils.gen32UUID();
            RedisUtil.set(RedisCacheConstants.LOGIN_ID + loginId, privateKey, 300l);

            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("loginId", loginId);
            dataMap.put("publicKey", publicKey);
            return Response.ok().wrap(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.error("获取公钥失败");
    }

    @PostMapping("/login")
    public Response login(@RequestBody SysUserDto recData, HttpServletResponse httpServletResponse){
        String account = recData.getAccount();
        String encryptPassword = recData.getEncryptPassword();
        String loginId = recData.getLoginId();
        String captchaId = recData.getCaptchaId();
        String captcha = recData.getCaptcha();
        if (StringUtils.isEmpty(account)
                ||
                StringUtils.isEmpty(loginId)
                ||
                StringUtils.isEmpty(encryptPassword)
                ||
                StringUtils.isEmpty(captchaId)
                ||
                StringUtils.isEmpty(captcha)
        ) {
            return Response.error("请求参数不完整");
        }

        //校验验证码
        String redisCaptcha = RedisUtil.get(RedisCacheConstants.CAPTCHA_ID + captchaId).toString();
        if (StringUtils.isEmpty(redisCaptcha)) {
            return Response.error("验证码过期");
        }
        if (!redisCaptcha.equalsIgnoreCase(captcha)) {
            return Response.error("验证码输入错误");
        }

        SysUserDto sysUserDto = this.sysUserService.selectByAccount(account);
        if (sysUserDto == null) {
            return Response.error("该帐号不存在");
        }

        //校验密码
        String privateKey = RedisUtil.get(RedisCacheConstants.LOGIN_ID + loginId).toString();
        if (StringUtils.isEmpty(privateKey)) {
            //因为loginId 5分钟过期
            return Response.error("页面已过期请刷新后再试");
        }
        String password = null;
        try {
            password = new String(RSAUtils.decryptByPrivateKey(Base64.getDecoder().decode(encryptPassword), privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sysUserDto == null || !sysUserDto.getPassword().equals(MD5.md5(password))) {
            return Response.error("账号或者密码错误");
        }

        if (Objects.equals(sysUserDto.getStatus(), new Byte("0"))) {
            return Response.error("账号禁用");
        }

        //私钥已经没有用了
        RedisUtil.del(RedisCacheConstants.LOGIN_ID + loginId);

        // 清除可能存在的Shiro权限信息缓存
        if (RedisUtil.hasKey(Constant.PREFIX_SHIRO_CACHE + sysUserDto.getAccount())) {
            RedisUtil.del(Constant.PREFIX_SHIRO_CACHE + sysUserDto.getAccount());
        }
        // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        RedisUtil.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + sysUserDto.getAccount(), currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));
        // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
        String token = JwtUtil.sign(sysUserDto.getAccount(), currentTimeMillis);
        httpServletResponse.setHeader("Authorization", token);
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        SysUserDto sysUser=new SysUserDto();
        sysUser.setId(sysUserDto.getId());
        sysUser.setAccount(account);
        List<SysRoleDto> sysRoleDtos = sysUserService.selectById(sysUserDto.getId()).getUserRoleList();
        if (!CollectionUtils.isEmpty(sysRoleDtos)){
            sysUser.setRoleName(sysRoleDtos.get(0).getRoleCnName());
        }
        Map<String, Object> map = new HashMap<String, Object>(){{
            put("userInfo",JSON.toJSONString(sysUser));
            put("token",token);
        }};
        return Response.ok().wrap(map);
    }

    @PostMapping("/logout")
    public Response logout(){
        // 获取当前登录用户
        SysUserDto sysUserDto = userUtil.getUser();
      /*  // 获取当前登录用户Id
        Long id = userUtil.getUserId();
        // 获取当前登录用户Token
        String token = userUtil.getToken();
        // 获取当前登录用户Account
        String account = userUtil.getAccount();*/
        //清除登录的缓存
        if (RedisUtil.hasKey(Constant.PREFIX_SHIRO_CACHE + sysUserDto.getAccount())) {
            RedisUtil.del(Constant.PREFIX_SHIRO_CACHE + sysUserDto.getAccount());
        }
        return Response.ok();
    }

    @GetMapping("getModuleList")
    public Response getModuleList(){
        List<SysMenuDto> result = new ArrayList<>();
        Long userId = userUtil.getUserId();
        List<SysRoleDto> userRoleList = sysUserService.selectById(userId).getUserRoleList();
        for (SysRoleDto sysRoleDto : userRoleList) {
            List<SysMenuDto> roleMenuList = sysRoleService.selectZeroMenuById(sysRoleDto.getId()).getRoleMenuList();
            for (SysMenuDto sysMenuDto : roleMenuList) {
                if (sysMenuDto.isChecked()) {
                    if (!result.contains(sysMenuDto)) {
                        result.add(sysMenuDto);
                    }
                }
            }
        }
        return Response.ok().wrap(result);
    }

    /**
     * 获取用户菜单
     */
    @GetMapping("getMenuList")
    public Response getMenuList() {
        List<SysMenuDto> result = new ArrayList<>();
        Long userId = userUtil.getUserId();
        List<SysRoleDto> userRoleList = sysUserService.selectById(userId).getUserRoleList();
        for (SysRoleDto sysRoleDto : userRoleList) {
            List<SysMenuDto> userMenuList = null;
            userMenuList = sysRoleService.selectById(sysRoleDto.getId()).getRoleMenuList();
            for (SysMenuDto sysMenuDto : userMenuList) {
                if (sysMenuDto.isChecked()) {
                    //得到用户角色所有的菜单
                    if (!result.contains(sysMenuDto)) {
                        //去重 已经重写equals
                        result.add(sysMenuDto);
                    }
                }
            }
        }
        return Response.ok().wrap(result);
    }

    /**
     * 修改密码
     * @param param
     * @return
     */
    @PostMapping("updatePassword")
    public Response updatePassword(@RequestBody HashMap param) {
        Long userId = userUtil.getUserId();
        if (userId != null) {
            if(StringUtil.isNotBlank(String.valueOf(param.get("password") + ""))){
                sysUserService.updatePassword(userId, String.valueOf(param.get("password") + ""));
            }else{
                return Response.error("密码不能为空");
            }
        }
        return Response.ok();

    }

}
