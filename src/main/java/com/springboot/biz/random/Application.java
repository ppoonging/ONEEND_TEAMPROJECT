package com.springboot.biz.random;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 스케줄링 기능 활성화
public class Application {
    public static void main(String[] args) {


        System.out.println("렌덤 돌리기 됨?????");
        SpringApplication.run(Application.class, args);
    }
}
