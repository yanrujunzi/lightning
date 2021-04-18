package com.healthy.controller;

import com.uni.user.dto.SysUserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @auto yangkai
 * @date 2019/10/22
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 身份认证测试接口
     * @param request
     * @return
     */
    @RequestMapping("/admin")
    public String admin(HttpServletRequest request) {
        Object user = request.getSession().getAttribute("user");
        return "success_view";
    }

    /**
     * 角色认证测试接口
     * @param request
     * @return
     */
    @RequestMapping("/student")
    public String student(HttpServletRequest request) {
        return "success_view";
    }

    /**
     * 权限认证测试接口
     * @param request
     * @return
     */
    @RequestMapping("/teacher")
    public String teacher(HttpServletRequest request) {
        return "success_view";
    }

    /**
     * 用户登录接口
     *
     * @param user    user
     * @param request request
     * @return string
     */
    @PostMapping("/login")
    public String login(SysUserDto user, HttpServletRequest request) {
        System.out.println("进入登录方法");
        // 获取 subject 认证主体
        Subject subject = SecurityUtils.getSubject();
        System.out.println("获取主题");
        // 根据用户名和密码创建 Token
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        System.out.println("获取token:"+token);
        try {
            // 开始认证，这一步会跳到我们自定义的 Realm 中
            subject.login(token);
            request.getSession().setAttribute("user", user);
            return "success_view";
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("user", user);
            request.setAttribute("error", "用户名或密码错误！");
            return "login_view";
        }
    }

}
