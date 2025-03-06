package com.springboot.biz.root.rootUser;

import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root")
public class RootAuthController {

    private final RootAuthService rootAuthService;
    private final RootService rootService;

    // 글 등록
//    @GetMapping("/form")
//    public String register(Model model) {
//
//        List<Root> rootList =  this.rootAuthService.get();
//        model.addAttribute("rootList", rootList);
//
//        return "/main/root/user/root_form_user";
//    }

    @GetMapping("/form")
    public String getRootForm(@RequestParam(value = "rootSeq", required = false) Integer rootSeq, Model model) {
        List<Root> root = rootService.getList();
        model.addAttribute("root", root);

        if(rootSeq != null) {
            Root selRoot = rootService.get(rootSeq);
            model.addAttribute("selRoot", selRoot);
            model.addAttribute("selRootList", selRoot.getRootList());
        }

        return "/main/root/user/root_form_user";
    }

    @GetMapping("/list")
    public String list() {
        return "/main/root/user/root_list_user";
    }
}
