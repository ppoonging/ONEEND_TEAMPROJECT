package com.springboot.biz;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root(){
        return "fragments/main";
    }

    @GetMapping("/free")
    public String free() {
        return "main/free/main_layout";
    }
}
