package com.springboot.biz.weather;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
        private String name = "Unknown"; // 기본값
        private Main main = new Main(); // 기본값 설정
        private Weather[] weather = new Weather[]{ new Weather() }; // 기본값 설정

        @Getter
        @Setter
        public static class Main {
                private double temp = 0.0; // 기본값 설정
                private double humidity = 0.0; // 기본값 설정
        }

        @Getter
        @Setter
        public static class Weather {
                private String description = "정보 없음"; // 기본값
                private String icon = "01d"; // 기본 아이콘
        }
}
