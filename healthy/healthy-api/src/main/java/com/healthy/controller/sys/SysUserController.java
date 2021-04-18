package com.healthy.controller.sys;


import com.alibaba.dubbo.config.annotation.Reference;
import com.healthy.controller.BaseController;
import com.uni.user.dto.SysUserDto;
import com.uni.user.service.SysUserService;
import com.uni.util.Page;
import com.uni.util.Request;
import com.uni.util.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("sysUser")
public class SysUserController extends BaseController {

    @Reference
    private SysUserService sysUserService;

    /**
	 * 列表
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="sysUser:list")
    public Response<Page> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Request request = Request.configParams(params).like("mobile");

        List<SysUserDto> list = sysUserService.queryList(params);
        int total  = sysUserService.queryTotal(params);
        if (list != null) {
            for (SysUserDto sysUserDto : list) {
            }
        }

        Page page = new Page(request.getPageNo(), request.getPageSize(), total, list);

        return Response.ok().wrap(page);
    }


    /**
     * 信息
     */
    @GetMapping("info/{id}")
    @RequiresPermissions(value ="sysUser:info")
    public Response info(@PathVariable("id") Long id){
        SysUserDto sysUserDto = sysUserService.selectById(id);

        return Response.ok().wrap(sysUserDto);
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @RequiresPermissions(value ="sysUser:edit")
    public Response save(@RequestBody SysUserDto sysUserDto){
        SysUserDto dbUser = sysUserService.selectByAccount(sysUserDto.getAccount());
        if (dbUser != null) {
            return Response.error("该账号已存在");
        }
        sysUserService.save(sysUserDto);
        return Response.ok();
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @RequiresPermissions(value ="sysUser:edit")
    public Response update(@RequestBody SysUserDto sysUserDto){
        SysUserDto dbUser = sysUserService.selectByAccount(sysUserDto.getAccount());
        if (dbUser != null && !Objects.equals(sysUserDto.getId(), dbUser.getId())) {
            return Response.error("您修改的账号已存在");
        }
        sysUserService.updateById(sysUserDto);
        return Response.ok();
    }
    /**
     * 修改
     */
    @PostMapping("resetPwd")
    @RequiresPermissions(value ="sysUser:reset")
    public Response resetPwd(@RequestBody Long[] ids){
        for (Long id : ids) {
            sysUserService.updatePassword(id ,"123456");
        }
        return Response.ok();
    }
    /**
	 * 删除
	 */
    @PostMapping("delete")
    @RequiresPermissions(value ="sysUser:del")
    public Response delete(@RequestBody Long[] ids){
        for (Long id : ids) {
            SysUserDto sysUserDto = sysUserService.selectById(id);
            if ("admin".equalsIgnoreCase(sysUserDto.getAccount())) {
                return Response.error("超级管理员不能被删除");
            }
        }
        sysUserService.deleteBatch(ids);
        return Response.ok();
    }
}
