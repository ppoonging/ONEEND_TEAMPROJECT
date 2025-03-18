package com.springboot.biz.weather;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String location, String apiKey) {
        // OpenWeather API URL
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey + "&units=metric&lang=kr";

        try {
            // REST API 호출하여 응답을 WeatherResponse 객체로 변환
            WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

            if (weatherResponse != null) {
                logger.info("Weather data for {}: Temp = {}°C, Humidity = {}%", weatherResponse.getName(), weatherResponse.getMain().getTemp(), weatherResponse.getMain().getHumidity());
            }

            return weatherResponse;
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
            return null;
        }
    }
    public WeatherResponse getWeatherByCoords(double lat, double lon, String apiKey) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric&lang=kr";

        try {
            WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

            if (weatherResponse != null) {
                logger.info("Weather data for {}: Temp = {}°C, Humidity = {}%", weatherResponse.getName(), weatherResponse.getMain().getTemp(), weatherResponse.getMain().getHumidity());
            }

            return weatherResponse;
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
            return null;
        }
    }


}

