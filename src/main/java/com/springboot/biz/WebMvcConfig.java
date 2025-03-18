package com.springboot.biz;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/");
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/files/mj");

//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("file:///home/ubuntu/deploy/files/"); // uploads로 바꿀 수도
//
//
//    registry.addResourceHandler("/files/mj/**")
//                .addResourceLocations("file:///home/ubuntu/deploy/files/mj"); // uploads로 바꿀 수도
//
//
//
//        registry.addResourceHandler("/files/sns/**")
//                .addResourceLocations("file:///home/ubuntu/deploy/files/sns"); // uploads로 바꿀 수도


    }
}
