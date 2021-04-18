package com.healthy.controller;

import com.healthy.constants.RedisCacheConstants;
import com.healthy.util.RedisUtil;
import com.uni.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("common")
public class CommonController extends BaseController {

    /**
     * 创建验证码
     */
    @GetMapping("createCaptcha")
    public Response createCaptcha() throws IOException {
        String captchaId = SnowFlake.nextId()+"";
        String captchaCode = RandomUtils.genCharsCode(4);

        RedisUtil.set(RedisCacheConstants.CAPTCHA_ID + captchaId, captchaCode, 120l);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        CaptchaUtils.outputImage(300,100, baos, captchaCode);

        Map<String, String> resp = new HashMap<>();
        resp.put("captchaId", captchaId);
        resp.put("imgBase64", ImageUtils.base64Encode(baos));

        return Response.ok().wrap(resp);
    }
}
