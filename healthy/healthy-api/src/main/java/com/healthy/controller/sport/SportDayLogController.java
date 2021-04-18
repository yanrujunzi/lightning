package com.healthy.controller.sport;


import com.healthy.controller.BaseController;
import com.healthy.sport.dto.SportDayLogDto;
import com.healthy.sport.service.SportDayLogService;
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
@RequestMapping("sportDayLog")
public class SportDayLogController extends BaseController {

    @Autowired
    private SportDayLogService sportDayLogService;

    @GetMapping("queryAll")
    @RequiresPermissions(value ="sportDayLog:list")
    public Response<Page<SportDayLogDto>> queryAll(){
        List<SportDayLogDto> sportDayLogDtos = sportDayLogService.queryList(new HashMap<>());
        return Response.ok().wrap(sportDayLogDtos);
    }

    /**
	 * 列表
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="sportDayLog:list")
    public Response<Page<SportDayLogDto>> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Request request = Request.configParams(params);
        int total = sportDayLogService.queryTotal(params);
        
        List<SportDayLogDto> list = null;
        if(total > 0){
            list = sportDayLogService.queryList(params);
        }else {
            list = new ArrayList<>();
        }

        Page<SportDayLogDto> page = new Page<SportDayLogDto>(request.getPageNo(), request.getPageNo(), total, list);
        return Response.ok().wrap(page);
    }


    /**
     * 信息
     */
    @GetMapping("info/{id}")
    @RequiresPermissions(value ="sportDayLog:info")
    public Response<SportDayLogDto> info(@PathVariable("id") Long id){
        SportDayLogDto sportDayLogDto = sportDayLogService.selectById(id);

        return Response.ok().wrap(sportDayLogDto);
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @RequiresPermissions(value ="sportDayLog:edit")
    public Response save(@RequestBody SportDayLogDto sportDayLogDto){
        //填充基础数据
        sportDayLogService.save(sportDayLogDto);
        return Response.ok();
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @RequiresPermissions(value ="sportDayLog:edit")
    public Response update(@RequestBody SportDayLogDto sportDayLogDto){
        sportDayLogService.updateById(sportDayLogDto);
        return Response.ok();
    }

    /**
	 * 删除
	 */
    @PostMapping("delete")
    @RequiresPermissions(value ="sportDayLog:delete")
    public Response delete(@RequestBody Long[] ids){
        sportDayLogService.deleteBatch(ids);
        return Response.ok();
    }
}
