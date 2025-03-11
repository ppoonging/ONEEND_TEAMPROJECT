package com.springboot.biz.weather;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
        private String name; // 도시 이름
        private Main main; // 날씨 정보
        private Weather[] weather; // 날씨 상세


        @Getter
        @Setter
        public static class Main {
                private double temp; // 기온
                private double humidity; // 습도
        }

        @Getter
        @Setter
        public static class Weather {
                private String description; // 날씨 설명
        }
}



