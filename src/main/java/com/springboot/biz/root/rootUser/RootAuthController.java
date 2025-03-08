package com.springboot.biz.root.rootUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootList;
import com.springboot.biz.root.rootAdmin.RootListDTO;
import com.springboot.biz.root.rootAdmin.RootService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root")
public class RootAuthController {

    private final RootAuthService rootAuthService;
    private final RootService rootService;
    private final HUserSerevice hUserSerevice;

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

    @PostMapping("/form/save")
    public String formSave(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
                           @RequestParam("content") String content, @RequestParam("rootList") String rootListJson,
                           @RequestParam("rootSeq") Integer rootSeq,
                           Principal principal) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<RootAuthListDTO> rootList = new ArrayList<>();

        try {
            rootList = objectMapper.readValue(rootListJson, new TypeReference<List<RootAuthListDTO>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HUser user = this.hUserSerevice.getUser(principal.getName());

        for (RootAuthListDTO list : rootList) {
            System.out.println(list.getAddress());
            System.out.println(list.getCategory());

        }

        Root root = rootService.get(rootSeq);



        rootAuthService.save(file, title, content, rootList, user, root);

        return "redirect:/root/list";
    }
}
