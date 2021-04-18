package com.healthy.controller;

import com.uni.util.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("hello")
    public Response hello(){
        return Response.ok().wrap("hello word");
    }

}
