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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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



    @GetMapping("/list")
    public String list(Model model, @RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(required = false) String chkCategory) {

        Page<RootAuth> rootAuthPage;

        System.out.println(chkCategory);

        if (chkCategory == null || chkCategory.equals("전체")) {
            rootAuthPage = this.rootAuthService.getAll(page);
        } else {
            rootAuthPage = this.rootAuthService.getAllCategory(page, chkCategory);
        }

        List<Root> root = this.rootService.getList();

        model.addAttribute("root", root);
        model.addAttribute("rootAuthPage", rootAuthPage);
        model.addAttribute("chkCategory", chkCategory); // 선택한 값 유지

        return "/root/user/root_list_user";
    }

    @GetMapping("/form")
    public String getRootForm(@RequestParam(value = "rootSeq", required = false) Integer rootSeq, Model model) {
        List<Root> root = rootService.getList();
        model.addAttribute("root", root);

        if(rootSeq != null) {
            Root selRoot = rootService.get(rootSeq);
            model.addAttribute("selRoot", selRoot);
            model.addAttribute("selRootList", selRoot.getRootList());
        }

        return "/root/user/root_form_user";
    }

//    @GetMapping("/form/save")
//    public String formSave(Principal principal, RootAuthDTO rootAuthDTO, Model model) {
//
//        HUser user = hUserSerevice.getUser(principal.getName());
//        model.addAttribute("user", user);
//        model.addAttribute("rootAuthDTO", new RootAuthDTO());
//
//        return "/root/user/root_form_user";
//    }
//
//    @PostMapping("/form/save")
//    public String formSave(Principal principal, @Valid @ModelAttribute RootAuthDTO rootAuthDTO, BindingResult bindingResult, Model model) throws IOException {
//
//        if(bindingResult.hasErrors()){
//            List<Root> root = rootService.getList();
//            model.addAttribute("root", root);
//            model.addAttribute("rootAuthDTO", rootAuthDTO);
//
//            return "/root/user/root_form_user";
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<RootAuthListDTO> rootList = new ArrayList<>();
//
//        try {
//            rootList = objectMapper.readValue(rootAuthDTO.getRootList(), new TypeReference<List<RootAuthListDTO>>() {});
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        // 현재 로그인한 사용자 정보 가져오기
//        HUser user = hUserSerevice.getUser(principal.getName());
//
//        // 루트 정보 가져오기
//        Root root = rootService.get(Integer.parseInt(rootAuthDTO.getRootSeq()));
//
//        // 서비스 로직 호출
//        rootAuthService.save(rootAuthDTO.getFiles(), rootAuthDTO.getTitle(), rootAuthDTO.getContent(), rootList, user, root);
//
//        return "redirect:/root/list";
//    }

    @PostMapping("/form/save")
    public String formSave(@RequestParam("files") List<MultipartFile> files, @RequestParam("title") String title,
                           @RequestParam("content") String content, @RequestParam("rootList") String rootListJson,
                           @RequestParam("rootSeq") Integer rootSeq,
                           Principal principal, @Valid @ModelAttribute RootAuthDTO rootAuthDTO, BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()){
            return "/root/user/root_form_user";
        }

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

        rootAuthService.save(files, title, content, rootList, user, root);

        return "redirect:/root/list";
    }

    @GetMapping("/detail/{rootAuthSeq}")
    public String detail(@PathVariable("rootAuthSeq") Long rootAuthSeq, Model model, Principal principal) {

        // 인증 유저
        RootAuth rootAuth = this.rootAuthService.get(rootAuthSeq);
        List<RootAuthList> rootAuthList = this.rootAuthService.getRootList(rootAuthSeq);

        // 등록된 어드민
        Root root = rootAuth.getRoot();
        List<RootList> rootList = root.getRootList();

        HUser user = this.hUserSerevice.getUser(principal.getName());

        model.addAttribute("user",user);
        model.addAttribute("rootAuthList", rootAuthList);
        model.addAttribute("rootAuth", rootAuth);
        model.addAttribute("rootList", rootList);

        return "/root/user/root_detail_user";
    }

    @PostMapping("/form/modify/{rootAuthSeq}")
    public String modify(@PathVariable("rootAuthSeq") Long rootAuthSeq) {

        RootAuth rootAuth = this.rootAuthService.get(rootAuthSeq);

        return String.format("redirect:/root/detail/%s", rootAuthSeq);
    }
}
