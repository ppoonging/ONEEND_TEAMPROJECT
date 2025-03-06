package com.springboot.biz.root.rootAdmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/register")
public class RootController {

    private final RootService rootService;
    private final HUserSerevice hUserSerevice;

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

//    @GetMapping("/form/save")
//    public String save() {
//        return "/main/root/admin/root_list_admin";
//    }

    @PostMapping("/form/save")
    public String saveRoot(@RequestParam("title") String title,
                           @RequestParam("rootList") String rootListJson,
                           Principal principal) {

        System.out.println("제목: " + title);
        System.out.println("rootList JSON: " + rootListJson);

        // JSON 문자열을 Java List<RootListDTO>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<RootListDTO> rootList = new ArrayList<>();

        try {
            rootList = objectMapper.readValue(rootListJson, new TypeReference<List<RootListDTO>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HUser user = this.hUserSerevice.getUser(principal.getName());
        rootService.save(title, rootList, user);

        return "redirect:/root/register/list"; // 저장 후 리다이렉트
    }

    @GetMapping("/detail/{rootSeq}")
    public String detail(@PathVariable("rootSeq") Integer rootSeq, Model model) {

        Root root = this.rootService.get(rootSeq);
        model.addAttribute("root", root);

        return "/main/root/admin/root_detail_admin";
    }

}
