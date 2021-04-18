package com.healthy.controller.food;


import com.healthy.controller.BaseController;
import com.healthy.food.dto.FoodManageDto;
import com.healthy.food.service.FoodManageService;
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
@RequestMapping("foodManage")
public class FoodManageController extends BaseController {

    @Autowired
    private FoodManageService foodManageService;

    @GetMapping("queryAll")
    @RequiresPermissions(value ="foodManage:list")
    public Response<Page<FoodManageDto>> queryAll(){
        List<FoodManageDto> foodManageDtos = foodManageService.queryList(new HashMap<>());
        return Response.ok().wrap(foodManageDtos);
    }

    /**
	 * 列表
	 */
    @GetMapping("list")
    @RequiresPermissions(value ="foodManage:list")
    public Response<Page<FoodManageDto>> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Request request = Request.configParams(params);
        int total = foodManageService.queryTotal(params);
        
        List<FoodManageDto> list = null;
        if(total > 0){
            list = foodManageService.queryList(params);
        }else {
            list = new ArrayList<>();
        }

        Page<FoodManageDto> page = new Page<>(request.getPageNo(), request.getPageNo(), total, list);
        return Response.ok().wrap(page);
    }


    /**
     * 信息
     */
    @GetMapping("info/{id}")
    @RequiresPermissions(value ="foodManage:info")
    public Response<FoodManageDto> info(@PathVariable("id") Long id){
        FoodManageDto foodManageDto = foodManageService.selectById(id);

        return Response.ok().wrap(foodManageDto);
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @RequiresPermissions(value ="foodManage:edit")
    public Response save(@RequestBody FoodManageDto foodManageDto){
        //填充基础数据
        foodManageService.save(foodManageDto);
        return Response.ok();
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @RequiresPermissions(value ="foodManage:edit")
    public Response update(@RequestBody FoodManageDto foodManageDto){
        foodManageService.updateById(foodManageDto);
        return Response.ok();
    }

    /**
	 * 删除
	 */
    @PostMapping("delete")
    @RequiresPermissions(value ="foodManage:delete")
    public Response delete(@RequestBody Long[] ids){
        foodManageService.deleteBatch(ids);
        return Response.ok();
    }
}
