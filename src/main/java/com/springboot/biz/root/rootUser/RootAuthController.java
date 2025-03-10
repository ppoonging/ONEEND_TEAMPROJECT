package com.springboot.biz.root.rootUser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootList;
import com.springboot.biz.root.rootAdmin.RootService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    public String list(Model model, @RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "chkCategory", defaultValue = "전체", required = false) String chkCategory) {

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

    @GetMapping("/delete/{rootAuthSeq}")
    public String delete(@PathVariable("rootAuthSeq") Long rootAuthSeq){
        this.rootAuthService.delete(rootAuthSeq);
        return "redirect:/root/list";
    }

    @GetMapping("/form")
    public String getRootForm(@RequestParam(value = "rootSeq", required = false) Integer rootSeq, Model model, RootAuthDTO rootAuthDTO) {
        List<Root> root = rootService.getList();
        model.addAttribute("root", root);


        if(rootSeq != null) {
            Root selRoot = rootService.get(rootSeq);
            model.addAttribute("selRoot", selRoot);
            model.addAttribute("selRootList", selRoot.getRootList());
            rootAuthDTO.setRootSeq(rootSeq);
        }

        return "root/user/root_form_user";
    }

    @PostMapping("/form/save")
    public String formSave(Principal principal, @Valid @ModelAttribute RootAuthDTO rootAuthDTO, BindingResult bindingResult, Model model) throws IOException {

        List<Root> root = rootService.getList();
        model.addAttribute("root", root); // 루트 목록 (select 용)

        Integer rootSeq = rootAuthDTO.getRootSeq();

        if (rootSeq != null) {
            Root selRoot = rootService.get(rootSeq);
            model.addAttribute("selRoot", selRoot);
            model.addAttribute("selRootList", selRoot.getRootList());
            rootAuthDTO.setRootSeq(rootSeq);
        }

        System.out.println("test" + rootAuthDTO.getRootAuthList());

        List<RootAuthListDTO> rootAuthList = new ArrayList<>();
        if (rootAuthDTO.getRootAuthList() != null && !rootAuthDTO.getRootAuthList().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                rootAuthList = objectMapper.readValue(rootAuthDTO.getRootAuthList(), new TypeReference<List<RootAuthListDTO>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // 에러 처리
            }
        }
        model.addAttribute("selRootUserList", rootAuthList);

        if (bindingResult.hasErrors()) {
            return "root/user/root_form_user"; // 실패 시 원래 화면으로
        }

        System.out.println("가지나?" + rootAuthList);

        // 정상 로직
        HUser user = this.hUserSerevice.getUser(principal.getName());
        Root rootEntity = rootService.get(rootAuthDTO.getRootSeq());

        rootAuthService.save(rootAuthDTO.getFiles(), rootAuthDTO.getTitle(), rootAuthDTO.getContent(), rootAuthList, user, rootEntity);

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


    @GetMapping("/form/modify/{rootAuthSeq}")
    public String modifyRootAuthForm(@PathVariable("rootAuthSeq") Long rootAuthSeq, Model model, RootAuthDTO rootAuthDTO, Principal principal) {

        HUser user = this.hUserSerevice.getUser(principal.getName());

        RootAuth rootAuth = this.rootAuthService.get(rootAuthSeq);

        if(!rootAuth.getUserId().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        rootAuthDTO.setRootSeq(rootAuth.getRoot().getRootSeq());
        rootAuthDTO.setTitle(rootAuth.getRootAuthTitle());
        rootAuthDTO.setContent(rootAuth.getRootAuthContent());
        rootAuthDTO.setRootAuthLists(rootAuth.getRootAuthList());

        System.out.println("test: " + rootAuth.getRootAuthList().get(0).getRootAuthListAddress());

        Integer rootSeq = rootAuthDTO.getRootSeq();

        if (rootSeq != null) {
            Root selRoot = rootService.get(rootSeq);
            model.addAttribute("selRoot", selRoot);
            model.addAttribute("selRootList", selRoot.getRootList());
            rootAuthDTO.setRootSeq(rootSeq);
        }

        model.addAttribute("rootAuthList", rootAuthDTO.getRootAuthLists());
        model.addAttribute("root", rootService.getList());
        model.addAttribute("rootAuthSeq", rootAuthSeq);

        return "/root/user/root_form_user";
    }

    @PostMapping("/form/modify/{rootAuthSeq}")
    public String modify(@PathVariable("rootAuthSeq") Long rootAuthSeq) {

        RootAuth rootAuth = this.rootAuthService.get(rootAuthSeq);

        return String.format("redirect:/root/detail/%s", rootAuthSeq);
    }
}
