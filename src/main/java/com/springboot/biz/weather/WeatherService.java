package com.springboot.biz.weather;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    //로그를 찍어 오류를 찾기 쉽게 한다.
    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String location, String apiKey) {
        // OpenWeather API URL
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + location + "&appid=" + apiKey + "&units=metric&lang=kr";

//"https://api.openweathermap.org/data/2.5/weather?q이게 사이트에서 제공해주는 기본주소

        try {
            WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);
            if (weatherResponse != null) {
                logger.info("Weather data for {}: Temp = {}°C, Humidity = {}%",
                        weatherResponse.getName(),
                        weatherResponse.getMain().getTemp(),
                        weatherResponse.getMain().getHumidity());
                return weatherResponse;
            }
            logger.error("Weather API returned null for location: " + location);
            return new WeatherResponse(); // 빈 객체 반환하여 NPE 방지
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
            return new WeatherResponse(); // 오류 발생 시 빈 객체 반환
        }

    }
}
