package com.healthy.controller.body;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.healthy.body.dto.BodyManageDto;
import com.healthy.body.service.BodyManageService;
import com.healthy.controller.BaseController;
import com.uni.user.dto.SysUserDto;
import com.uni.util.Page;
import com.uni.util.Request;
import com.uni.util.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("bodyManage")
public class BodyManageController extends BaseController {

    @Autowired
    private BodyManageService bodyManageService;

    @GetMapping("queryAll")
    @RequiresPermissions(value ="bodyManage:list")
    public Response<Page<BodyManageDto>> queryAll(){
        List<BodyManageDto> bodyManageDtos = bodyManageService.queryList(new HashMap<>());
        return Response.ok().wrap(bodyManageDtos);
    }

    /**
	 * 列表
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="bodyManage:list")
    public Response<Page<BodyManageDto>> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Request request = Request.configParams(params);
        SysUserDto sysUserDto=userUtil.getUser();
        params.put("idUser",sysUserDto.getId());
        int total = bodyManageService.queryTotal(params);
        
        List<BodyManageDto> list = null;
        if(total > 0){
            list = bodyManageService.queryList(params);
        }else {
            list = new ArrayList<>();
        }

        Page<BodyManageDto> page = new Page<BodyManageDto>(request.getPageNo(), request.getPageNo(), total, list);
        return Response.ok().wrap(page);
    }


    /**
     * 信息
     */
    @GetMapping("info/{id}")
    @RequiresPermissions(value ="bodyManage:info")
    public Response<BodyManageDto> info(@PathVariable("id") Long id){
        BodyManageDto bodyManageDto = bodyManageService.selectById(id);

        return Response.ok().wrap(bodyManageDto);
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @RequiresPermissions(value ="bodyManage:edit")
    public Response save(@RequestBody BodyManageDto bodyManageDto){
        //查询用户信息
        SysUserDto sysUserDto=userUtil.getUser();
        bodyManageDto.setIdUser(sysUserDto.getId());
        //填充基础数据
        bodyManageService.save(bodyManageDto);
        return Response.ok();
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @RequiresPermissions(value ="bodyManage:edit")
    public Response update(@RequestBody BodyManageDto bodyManageDto){
        bodyManageService.updateById(bodyManageDto);
        return Response.ok();
    }

    /**
	 * 删除
	 */
    @PostMapping("delete")
    @RequiresPermissions(value ="bodyManage:delete")
    public Response delete(@RequestBody Long[] ids){
        bodyManageService.deleteBatch(ids);
        return Response.ok();
    }
}
