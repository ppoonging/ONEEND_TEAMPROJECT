package com.springboot.biz.root.rootUser;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        return "root/user/root_list_user";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/form/delete/{rootAuthSeq}")
    public String delete(@PathVariable("rootAuthSeq") Long rootAuthSeq){
        this.rootAuthService.delete(rootAuthSeq);
        return "redirect:/root/list";
    }

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
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
    public String detail(@PathVariable("rootAuthSeq") Long rootAuthSeq, Model model) {

        // 인증 유저
        RootAuth rootAuth = this.rootAuthService.get(rootAuthSeq);
        List<RootAuthList> rootAuthList = this.rootAuthService.getRootList(rootAuthSeq);

        // 등록된 어드민
        Root root = rootAuth.getRoot();
        List<RootList> rootList = root.getRootList();




        model.addAttribute("rootAuthList", rootAuthList);
        model.addAttribute("rootAuth", rootAuth);
        model.addAttribute("rootList", rootList);

        return "root/user/root_detail_user";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/form/modify/{rootAuthSeq}")
    public String formModify(@PathVariable("rootAuthSeq") Long rootAuthSeq, Model model, RootAuthDTO rootAuthDTO, Principal principal) {

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

        List<RootAuthListDTO> simpleList = rootAuth.getRootAuthList().stream()
                .map(list -> new RootAuthListDTO(
                        list.getRootAuthListTitle(),
                        list.getRootAuthListAddress(),
                        list.getRootAuthListRodeAddress(),
                        list.getRootAuthListLatitude(),
                        list.getRootAuthListLongitude(),
                        list.getRootAuthListLink(),
                        list.getRootAuthListCategory(),
                        list.getRootAuthListImageName(),
                        list.getRootAuthListImagePath()
                )).toList();

        Integer rootSeq = rootAuthDTO.getRootSeq();

        if (rootSeq != null) {
            Root selRoot = rootService.get(rootSeq);
            model.addAttribute("selRoot", selRoot);
            model.addAttribute("selRootList", selRoot.getRootList());
            rootAuthDTO.setRootSeq(rootSeq);
        }

        model.addAttribute("rootAuthList", simpleList);
        model.addAttribute("root", rootService.getList());
        model.addAttribute("rootAuthSeq", rootAuthSeq);

        return "root/user/root_form_user";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/form/modify/{rootAuthSeq}")
    public String modify(@PathVariable("rootAuthSeq") Long rootAuthSeq, RootAuthDTO rootAuthDTO,
                         BindingResult bindingResult, Model model, Principal principal) throws IOException {

        HUser user = this.hUserSerevice.getUser(principal.getName());
        RootAuth rootAuth = this.rootAuthService.get(rootAuthSeq);

        if(!rootAuth.getUserId().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        Root rootEntity = rootService.get(rootAuthDTO.getRootSeq());

        List<RootAuthListDTO> rootAuthList = new ArrayList<>();
        if (rootAuthDTO.getRootAuthList() != null && !rootAuthDTO.getRootAuthList().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                rootAuthList = objectMapper.readValue(rootAuthDTO.getRootAuthList(), new TypeReference<List<RootAuthListDTO>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("root", rootService.getList());
            Root selRoot = rootService.get(rootAuthDTO.getRootSeq());
            model.addAttribute("selRoot", selRoot);
            model.addAttribute("selRootList", selRoot.getRootList());
            model.addAttribute("selRootUserList", rootAuthList);
            return "root/user/root_form_user";
        }

        System.out.println("리스트 오나 테스트 " + rootAuthList);

        rootAuthService.modify(rootAuthDTO.getFiles(), rootAuthDTO.getTitle(), rootAuthDTO.getContent(), rootAuthList, user, rootEntity, rootAuthSeq);

        return String.format("redirect:/root/detail/%s", rootAuthSeq);
    }
}
