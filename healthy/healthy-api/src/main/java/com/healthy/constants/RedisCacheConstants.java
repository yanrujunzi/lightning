package com.healthy.constants;

public class RedisCacheConstants {
    private static final String PROJECT_NAME = "BASE_API_";
    public static final String TOKEN_PREFIX = PROJECT_NAME + "SYS_USER_TOKEN:";
    public static final String VERSION_NAME = PROJECT_NAME + "VERSION_ID";
    /**
     * 登录的时候返回给前端  key为md5  value私钥 公钥返回给前端
     */
    public static String LOGIN_ID = PROJECT_NAME + "LOGIN_ID:";

    public static String CAPTCHA_ID = PROJECT_NAME + "CAPTCHA_ID";

}

