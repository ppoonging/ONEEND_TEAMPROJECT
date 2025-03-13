package com.springboot.biz.weather;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(required = false) String location, Model model) {
        if (location == null || location.trim().isEmpty()) {
            model.addAttribute("error", "Location is required!");
            return "fragments/main";  // weather.html로 돌아감
        }

        // 기본 API 키를 설정하거나, properties 파일로부터 읽어올 수 있습니다.
        String apiKey = "4bb66d8059b9e367d1207764e32daad2";  // API 키는 환경 설정 파일에 저장하거나 외부에서 관리하는 것이 좋습니다.

        // 날씨 정보 조회
        WeatherResponse weatherResponse = weatherService.getWeather(location, apiKey);
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();
        // 지역 이름을 한글로 변환
        String koreanLocation = changeKor(location);



        // 모델에 날씨 정보 추가
        model.addAttribute("weatherData", weatherResponse);
        model.addAttribute("today", today); // 오늘 날짜 추가
        model.addAttribute("location", koreanLocation); // 한글 지역 이름 추가
        return "fragments/main";  // weather.html을 반환
    }

    private String changeKor(String location) {
        switch (location.toLowerCase()) {
            case "seoul": return "서울";
            case "incheon": return "인천";
            case "gyeonggi_do": return "경기도";
            case "gangwon": return "강원도";
            case "gyeongsang": return "경상도";
            case "jeolla": return "전라도";
            case "busan": return "부산";
            case "daegu": return "대구";
            default:return location;//나머지는 영어로
        }
    }
}
