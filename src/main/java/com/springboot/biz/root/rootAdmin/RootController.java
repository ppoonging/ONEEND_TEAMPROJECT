package com.springboot.biz.root.rootAdmin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/register")
public class RootController {

    private final RootService rootService;

    @GetMapping("/form")
    public String tour() {
        return "/main/root/admin/root_form_admin";
    }

    @GetMapping("/form/search")
    public String search(@RequestParam(value = "query", defaultValue = "") String query, Model model){
        if(!query.isEmpty()) {
            List<Map<String, String>> searchData = rootService.search(query);
            System.out.println(searchData);
            model.addAttribute("searchData", searchData);
            model.addAttribute("query", query);
        }
        return "/main/root/admin/root_form_admin";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Root> list = this.rootService.getList();
        model.addAttribute("list", list);
        return "/main/root/admin/root_list_admin";
    }
}
