package com.healthy;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class HealthyApiApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthyApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HealthyApiApplication.class, args);
        LOGGER.info("本项目可以申请最大的内存为:{}m", Runtime.getRuntime().maxMemory() / 1024 / 1024);
    }

}
