package com.springboot.biz.root.rootAdmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.biz.root.rootUser.RootAuth;
import com.springboot.biz.root.rootUser.RootAuthDTO;
import com.springboot.biz.root.rootUser.RootAuthListDTO;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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



    @GetMapping("/list")
    public String list(Model model) {
        List<Root> list = this.rootService.getList();
        model.addAttribute("list", list);
        return "/root/admin/root_list_admin";
    }

//    @GetMapping("/form/save")
//    public String save() {
//        return "/main/root/admin/root_list_admin";
//    }

    @GetMapping("/form/delete/{rootSeq}")
    public String delete(@PathVariable("rootSeq") Integer rootSeq) {
        this.rootService.delete(rootSeq);
        return "redirect:/root/register/list";
    }


    @GetMapping("/detail/{rootSeq}")
    public String detail(@PathVariable("rootSeq") Integer rootSeq, Model model) {

        Root root = this.rootService.get(rootSeq);
        List<RootList> rootList = this.rootService.getRootList(rootSeq);

        model.addAttribute("rootList", rootList);
        model.addAttribute("root", root);

        return "/root/admin/root_detail_admin";
    }

//    @GetMapping("/form/search")
//    public String search(@RequestParam(value = "query", defaultValue = "") String query, Model model, RootDTO rootDTO){
//        if(!query.isEmpty()) {
//            List<Map<String, String>> searchData = rootService.search(query);
//            System.out.println(searchData);
//            model.addAttribute("searchData", searchData);
//            model.addAttribute("query", query);
//        }
//        return "/root/admin/root_form_admin";
//    }

    @GetMapping("/form/search")
    @ResponseBody  // 💥 이거 추가!!
    public List<Map<String, String>> search(@RequestParam String query) {
        return rootService.search(query); // JSON 형태로 반환
    }

    @GetMapping("/form")
    public String tour(RootDTO rootDTO, Model model) {

        return "/root/admin/root_form_admin";
    }


    @PostMapping("/form/save")
    public String rootSave(RootDTO rootDTO,
                           Principal principal, BindingResult bindingResult) {

//        System.out.println("제목: " + title);
//        System.out.println("rootList JSON: " + rootListJson);

        if (bindingResult.hasErrors()) {
            return "root/admin/root_form_admin"; // 실패 시 원래 화면으로
        }

        // JSON 문자열을 Java List<RootListDTO>로 변환
        List<RootListDTO> rootList = new ArrayList<>();
        if (rootDTO.getRootList() != null && !rootDTO.getRootList().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                rootList = objectMapper.readValue(rootDTO.getRootList(), new TypeReference<List<RootListDTO>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // 에러 처리
            }
        }

        HUser user = this.hUserSerevice.getUser(principal.getName());
        rootService.save(rootDTO.getTitle(), rootList, user);

        return "redirect:/root/register/list"; // 저장 후 리다이렉트
    }

    @GetMapping("/form/modify/{rootSeq}")
    public String rootModify(@PathVariable("rootSeq") Integer rootSeq, Model model, RootDTO rootDTO, Principal principal) {

        HUser user = this.hUserSerevice.getUser(principal.getName());
        Root root = this.rootService.get(rootSeq);
        List<RootList> rootList = this.rootService.getRootList(rootSeq);

        if(!root.getUserId().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        rootDTO.setTitle(root.getRootTitle());
        rootDTO.setRootLists(root.getRootList());

        List<RootListDTO> simpleList = root.getRootList().stream()
                        .map(list -> new RootListDTO(
                                list.getRootListTitle(),
                                list.getRootListAddress(),
                                list.getRootListRodeAddress(),
                                list.getRootListLatitude(),
                                list.getRootListLongitude(),
                                list.getRootListLink(),
                                list.getRootListCategory()
                        )).toList();


        model.addAttribute("rootList", simpleList);
        model.addAttribute("root", root);
        model.addAttribute("rootSeq", rootSeq);


        return "/root/admin/root_form_admin";
    }

    @PostMapping("/form/modify/{rootSeq}")
    public String modify(@PathVariable("rootSeq") Integer rootSeq, Model model, RootDTO rootDTO, Principal principal, BindingResult bindingResult) {

        HUser user = this.hUserSerevice.getUser(principal.getName());
        Root root = this.rootService.get(rootSeq);

        if(!root.getUserId().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        List<RootListDTO> rootList = new ArrayList<>();
        if (rootDTO.getRootList() != null && !rootDTO.getRootList().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                rootList = objectMapper.readValue(rootDTO.getRootList(), new TypeReference<List<RootListDTO>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // 파싱 실패 시
            }
        }

        System.out.println("리스트오니? " + rootList);

        if (bindingResult.hasErrors()) {
            model.addAttribute("rootList", rootList);
            model.addAttribute("root", root);
            model.addAttribute("rootSeq", rootSeq);
            return "/root/admin/root_form_admin";
        }


        rootService.modify(rootSeq, rootDTO.getTitle(), rootList, user);

        return String.format("redirect:/root/register/detail/%s", rootSeq);
    }

}
