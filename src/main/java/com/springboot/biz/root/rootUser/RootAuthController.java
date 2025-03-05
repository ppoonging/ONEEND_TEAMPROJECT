package com.springboot.biz.root.rootUser;

import com.springboot.biz.root.rootAdmin.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root")
public class RootAuthController {

    private final RootAuthService rootAuthService;

    // 글 등록
    @GetMapping("/form")
    public String register(Model model) {

        List<Root> rootList =  this.rootAuthService.get();
        model.addAttribute("rootList", rootList);

        return "/main/root/user/root_form_user";
    }

    @GetMapping("/list")
    public String list() {
        return "/main/root/user/root_list_user";
    }
}
