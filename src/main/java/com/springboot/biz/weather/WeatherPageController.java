package com.springboot.biz.weather;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weatherPage")
public class WeatherPageController {

    @GetMapping
    public String weatherPage() {
        return "weather";
    }
}
