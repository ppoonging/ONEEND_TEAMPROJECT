package com.springboot.biz;


import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.mj.board.MjboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MjboardService mjboardService;

    @GetMapping("/")
    public String root(Model model){

        List<Mjboard> mjboard = mjboardService.getList();
        model.addAttribute("mjboard", mjboard);

        return "fragments/main";
    }

    @GetMapping("/free")
    public String free() {
        return "main/free/main_layout";
    }

}
