package com.healthy.controller.food;


import com.healthy.controller.BaseController;
import com.healthy.food.dto.FoodDayLogDto;
import com.healthy.food.service.FoodDayLogService;
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
@RequestMapping("foodDayLog")
public class FoodDayLogController extends BaseController {

    @Autowired
    private FoodDayLogService foodDayLogService;

    @GetMapping("queryAll")
    @RequiresPermissions(value ="foodDayLog:list")
    public Response<Page<FoodDayLogDto>> queryAll(){
        List<FoodDayLogDto> foodDayLogDtos = foodDayLogService.queryList(new HashMap<>());
        return Response.ok().wrap(foodDayLogDtos);
    }

    /**
	 * 列表
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="foodDayLog:list")
    public Response<Page<FoodDayLogDto>> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Request request = Request.configParams(params);
        SysUserDto sysUserDto=userUtil.getUser();
        params.put("idUser",sysUserDto.getId());
        int total = foodDayLogService.queryTotal(params);
        List<FoodDayLogDto> list = null;
        if(total > 0){
            list = foodDayLogService.queryList(params);
        }else {
            list = new ArrayList<>();
        }

        Page<FoodDayLogDto> page = new Page<FoodDayLogDto>(request.getPageNo(), request.getPageNo(), total, list);
        return Response.ok().wrap(page);
    }


    /**
     * 信息
     */
    @GetMapping("info/{id}")
    @RequiresPermissions(value ="foodDayLog:info")
    public Response<FoodDayLogDto> info(@PathVariable("id") Long id){
        FoodDayLogDto foodDayLogDto = foodDayLogService.selectById(id);

        return Response.ok().wrap(foodDayLogDto);
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @RequiresPermissions(value ="foodDayLog:edit")
    public Response save(@RequestBody FoodDayLogDto foodDayLogDto){
        //查询用户信息
        SysUserDto sysUserDto=userUtil.getUser();
        foodDayLogDto.setIdUser(sysUserDto.getId());
        foodDayLogService.save(foodDayLogDto);
        return Response.ok();
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @RequiresPermissions(value ="foodDayLog:edit")
    public Response update(@RequestBody FoodDayLogDto foodDayLogDto){
        foodDayLogService.updateById(foodDayLogDto);
        return Response.ok();
    }

    /**
	 * 删除
	 */
    @PostMapping("delete")
    @RequiresPermissions(value ="foodDayLog:delete")
    public Response delete(@RequestBody Long[] ids){
        foodDayLogService.deleteBatch(ids);
        return Response.ok();
    }
}
