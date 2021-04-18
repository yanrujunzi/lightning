package com.healthy.controller;

import com.healthy.util.UserUtil;
import com.uni.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserUtil userUtil;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception e) {
        e.printStackTrace();
        logger.error("抛出异常", e);
        return Response.customize(500,"服务器好像出了点小问题");
    }

}
