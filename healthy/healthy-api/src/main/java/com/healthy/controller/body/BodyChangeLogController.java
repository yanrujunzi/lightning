package com.healthy.controller.body;


import com.healthy.body.dto.BodyChangeLogDto;
import com.healthy.body.service.BodyChangeLogService;
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
@RequestMapping("bodyChangeLog")
public class BodyChangeLogController extends BaseController {

    @Autowired
    private BodyChangeLogService bodyChangeLogService;

    @GetMapping("queryAll")
    @RequiresPermissions(value ="bodyChangeLog:list")
    public Response<Page<BodyChangeLogDto>> queryAll(){
        List<BodyChangeLogDto> bodyChangeLogDtos = bodyChangeLogService.queryList(new HashMap<>());
        return Response.ok().wrap(bodyChangeLogDtos);
    }

    /**
	 * 列表
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="bodyChangeLog:list")
    public Response<Page<BodyChangeLogDto>> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Request request = Request.configParams(params);
        SysUserDto sysUserDto=userUtil.getUser();
        params.put("idUser",sysUserDto.getId());
        int total = bodyChangeLogService.queryTotal(params);
        
        List<BodyChangeLogDto> list = null;
        if(total > 0){
            list = bodyChangeLogService.queryList(params);
        }else {
            list = new ArrayList<>();
        }

        Page<BodyChangeLogDto> page = new Page<BodyChangeLogDto>(request.getPageNo(), request.getPageNo(), total, list);
        return Response.ok().wrap(page);
    }


    /**
     * 信息
     */
    @GetMapping("info/{id}")
    @RequiresPermissions(value ="bodyChangeLog:info")
    public Response<BodyChangeLogDto> info(@PathVariable("id") Long id){
        BodyChangeLogDto bodyChangeLogDto = bodyChangeLogService.selectById(id);

        return Response.ok().wrap(bodyChangeLogDto);
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @RequiresPermissions(value ="bodyChangeLog:edit")
    public Response save(@RequestBody BodyChangeLogDto bodyChangeLogDto){
        //填充基础数据
        //查询用户信息
        SysUserDto sysUserDto=userUtil.getUser();
        bodyChangeLogDto.setIdUser(sysUserDto.getId());
        bodyChangeLogService.save(bodyChangeLogDto);
        return Response.ok();
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @RequiresPermissions(value ="bodyChangeLog:edit")
    public Response update(@RequestBody BodyChangeLogDto bodyChangeLogDto){
        bodyChangeLogService.updateById(bodyChangeLogDto);
        return Response.ok();
    }

    /**
	 * 删除
	 */
    @PostMapping("delete")
    @RequiresPermissions(value ="bodyChangeLog:delete")
    public Response delete(@RequestBody Long[] ids){
        bodyChangeLogService.deleteBatch(ids);
        return Response.ok();
    }
}
