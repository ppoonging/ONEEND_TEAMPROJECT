package com.springboot.biz;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/files/mj/**")
                .addResourceLocations("file:src/main/resources/static/files/mj/");

        registry.addResourceHandler("/**")
                .addResourceLocations("file:src/main/resources/static/");


    }
}