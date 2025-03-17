package com.springboot.biz.map;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MapRestController {

    private final MapService mapService;

    @GetMapping("/direction")
    public Map<String, Object> get(@RequestParam String start, String goal){
        return mapService.directions(start, goal);
    }


}
