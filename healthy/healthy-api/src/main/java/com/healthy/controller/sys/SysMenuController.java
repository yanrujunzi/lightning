package com.healthy.controller.sys;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthy.controller.BaseController;
import com.uni.user.dto.SysMenuDto;
import com.uni.user.service.SysMenuService;
import com.uni.util.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sysMenu")
public class SysMenuController extends BaseController {

    @Reference
    private SysMenuService sysMenuService;
    /**
     * 拖拽节点
     */
    @PostMapping("drag")
    @RequiresPermissions(value ="sysMenu:edit")
    public Response drag(@RequestBody JSONObject params){
        Long targetNodeId = params.getLong("targetNodeId");
        JSONArray dragNodesId = params.getJSONArray("dragNodesId");
        for (int i = 0; i < dragNodesId.size(); i++) {
            Long dragNodeId = dragNodesId.getLong(i);
            SysMenuDto sysMenuDto = new SysMenuDto();
            sysMenuDto.setId(dragNodeId);
            sysMenuDto.setParentId(targetNodeId);
            sysMenuService.updateById(sysMenuDto);
        }
        return Response.ok();
    }
    /**
	 * 菜单树操作
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="sysMenu:list")
    public Response<List<SysMenuDto>> list(){
        List<SysMenuDto> list = sysMenuService.queryList();
        return Response.ok().wrap(list);
    }

    /**
     * 得到所有启用的菜单
     */
    @GetMapping("queryCanUsefulMenus")
    public Response<List<SysMenuDto>> queryCanUsefulMenus(){
        List<SysMenuDto> list = sysMenuService.queryCanUsefulMenus();
        return Response.ok().wrap(list);
    }


    /**
     * 信息
     */
    @GetMapping("info/{id}")
    @RequiresPermissions(value ="sysMenu:info")
    public Response info(@PathVariable("id") Long id){
        SysMenuDto sysMenuDto = sysMenuService.selectById(id);

        return Response.ok().wrap(sysMenuDto);
    }
    /**
     * 保存
     */
    @PostMapping("save")
    @RequiresPermissions(value ="sysMenu:edit")
    public Response save(@RequestBody SysMenuDto sysMenuDto){
        sysMenuService.save(sysMenuDto);
        return Response.ok();
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @RequiresPermissions(value ="sysMenu:edit")
    public Response update(@RequestBody SysMenuDto sysMenuDto){
        sysMenuService.updateById(sysMenuDto);
        return Response.ok();
    }

    /**
	 * 删除
	 */
    @PostMapping("delete")
    @RequiresPermissions(value ="sysMenu:delete")
    public Response delete(@RequestBody Long[] ids){
        String message=sysMenuService.deleteBatch(ids);
        return message!=null?Response.error(message):Response.ok();
    }
}
