package com.healthy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"dev"})
public class SwaggerConfig {
    @Autowired
    private ApplicationContext applicationContext;

}
