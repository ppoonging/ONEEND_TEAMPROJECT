package com.springboot.biz;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        // 정적 리소스 - JS, CSS, 이미지 등
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // 업로드 이미지 - 외부 디렉토리 (예: userRecipe, fmrecipe 등)
        registry.addResourceHandler("/files/userRecipe/**")
                .addResourceLocations("file:/home/ubuntu/oneend/files/userRecipe/");


        registry.addResourceHandler("/files/fmrecipe/**")
                .addResourceLocations("file:/home/ubuntu/oneend/files/fmrecipe/");
    }
}
