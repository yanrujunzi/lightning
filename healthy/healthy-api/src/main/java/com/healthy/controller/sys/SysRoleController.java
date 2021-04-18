package com.healthy.controller.sys;


import com.alibaba.dubbo.config.annotation.Reference;
import com.healthy.controller.BaseController;
import com.healthy.util.UserUtil;
import com.uni.user.dto.SysRoleDto;
import com.uni.user.dto.SysUserDto;
import com.uni.user.service.SysRoleService;
import com.uni.user.service.SysUserService;
import com.uni.util.Page;
import com.uni.util.Request;
import com.uni.util.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("sysRole")
public class SysRoleController extends BaseController {

    @Reference
    private SysRoleService sysRoleService;
    @Reference
    private SysUserService sysUserService;
    @Autowired
    private UserUtil userUtil;

    /**
	 * 列表
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="sysRole:list")
    public Response<Page> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Request.configParams(params);

        List<SysRoleDto> list = sysRoleService.queryList(params);
        int total  = sysRoleService.queryTotal(params);


        Page page = new Page(Integer.valueOf(params.get("pageNo")+""), Integer.valueOf(params.get("pageSize")+""), total, list);

        return Response.ok().wrap(page);
    }


    /**
     * 列表
     */
    @GetMapping("queryAllRoles")
    public Response<List<SysRoleDto>> queryAllRoles(){
        SysUserDto sysUserDto=userUtil.getUser();
        for(SysRoleDto sysRoleDto:sysUserDto.getUserRoleList()){
            if (sysRoleDto.getRoleName().equals("Super")) {
                //超级管理员查询所有的用户
                return Response.ok().wrap(sysRoleService.queryAllRoles());
            }
        }
        //非超级管理员查询自己管理的权限
        return Response.ok().wrap(sysUserDto.getUserRoleList());
    }

    /**
     * 信息
     */
    @RequestMapping("info/{id}")
    @RequiresPermissions(value ="sysRole:info")
    public Response info(@PathVariable("id") Long id){
        SysRoleDto sysRoleDto = sysRoleService.selectById(id);

        return Response.ok().wrap(sysRoleDto);
    }

    /**
     * 保存
     */
    @RequestMapping("save")
    @RequiresPermissions(value ="sysRole:edit")
    public Response save(@RequestBody SysRoleDto sysRoleDto){

        SysRoleDto dbRole = sysRoleService.selectByRoleName(sysRoleDto.getRoleName());
        if (dbRole != null) {
            return Response.error("该角色已经存在");
        }
        sysRoleService.save(sysRoleDto);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("update")
    @RequiresPermissions(value ="sysRole:edit")
    public Response update(@RequestBody SysRoleDto sysRoleDto){

        SysRoleDto dbRole = sysRoleService.selectByRoleName(sysRoleDto.getRoleName());
        if (dbRole != null && !Objects.equals(dbRole.getId(), sysRoleDto.getId())) {
            return Response.error("您修改的角色已经存在");
        }

        sysRoleService.updateById(sysRoleDto);

        return Response.ok();
    }

    /**
	 * 删除
	 */
    @RequestMapping("delete")
    @RequiresPermissions(value ="sysRole:delete")
    public Response delete(@RequestBody Long[] ids){
        for (Long id : ids) {
            SysRoleDto sysRoleDto = sysRoleService.selectById(id);
            if (Objects.equals(sysRoleDto.getRoleName(), "Super")) {
                return Response.error("超级管理员角色不能被删除");
            }
        }
        sysRoleService.deleteBatch(ids);
        return Response.ok();
    }
}
