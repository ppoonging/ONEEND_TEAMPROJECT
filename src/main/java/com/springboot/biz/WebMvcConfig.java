package com.springboot.biz;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Spring Boot 기본 정적 리소스 제공
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // 개별 정적 파일 경로 지정 (서버 내 특정 경로 추가)
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/", "file:/home/ubuntu/oneend/static/css/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/", "file:/home/ubuntu/oneend/static/images/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/", "file:/home/ubuntu/oneend/static/js/");

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
