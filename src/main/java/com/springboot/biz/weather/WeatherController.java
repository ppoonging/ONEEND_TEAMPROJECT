package com.springboot.biz.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weather")
public class WeatherController {


    private final WeatherService weatherService;
    private final String apiKey = "4bb66d8059b9e367d1207764e32daad2"; // API 키는 환경 변수로 관리하는 것이 좋음.

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Map<String, Object> getWeather(@RequestParam(value = "location", required = false, defaultValue = "Seoul") String location) {
        WeatherResponse weatherResponse = weatherService.getWeather(location, apiKey);

        Map<String, Object> response = new HashMap<>();
        if (weatherResponse == null) {
            response.put("error", "날씨 정보를 가져올 수 없습니다.");
            return response;
        }

        // 오늘 날짜 추가
        response.put("today", LocalDate.now().toString());
        response.put("location", changeKor(location));
        response.put("weatherData", weatherResponse);
        response.put("weatherIcon", "https://openweathermap.org/img/wn/" + weatherResponse.getWeather()[0].getIcon() + "@2x.png");

        return response;
    }

    private String changeKor(String location) {
        switch (location.toLowerCase()) {
            case "seoul": return "서울";
            case "incheon": return "인천";
            case "gyeonggi-do": return "경기도";
            case "busan": return "부산";
            default: return location;
        }
    }
}
