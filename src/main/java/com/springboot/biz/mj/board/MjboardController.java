package com.springboot.biz.mj.board;// ✳️ MjboardController.java

import com.springboot.biz.mj.answer.MjAnswerForm;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mjboard")
@EnableMethodSecurity(prePostEnabled = true)
public class MjboardController {

    private final HUserSerevice hUserSerevice;
    private final MjboardService mjboardService;

    // 게시판 리스트
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Mjboard> paging = this.mjboardService.getList(page);
        model.addAttribute("paging", paging);
        return "mj/mjboard_list";
    }

    // 글쓰기 폼 (GET)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String mjboardCreate(MjboardForm mjboardForm) {
        return "mj/mjboard_form";
    }

    // 글쓰기 저장 (POST)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String mjboardCreate(@Valid MjboardForm mjboardForm,
                                BindingResult bindingResult,
                                @RequestParam("file") MultipartFile file,
                                Principal principal) throws Exception {
        if (bindingResult.hasErrors()) {
            return "mj/mjboard_form";
        }

        // 로그인 사용자 정보 가져오기
        HUser hUser = this.hUserSerevice.getUser(principal.getName());
        this.mjboardService.create(mjboardForm.getMjTitle(), mjboardForm.getMjContent(), file, hUser);

        return "redirect:/mjboard/list";
    }

    // 썸머노트 이미지 업로드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String imageUrl = this.mjboardService.saveSummernoteImage(file);
        return ResponseEntity.ok(Collections.singletonMap("url", imageUrl));
    }

    // 상세 페이지
    @GetMapping("/detail/{mjSeq}")
    public String detail(Model model, @PathVariable("mjSeq") Integer mjSeq) {
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        model.addAttribute("mjboard", mjboard);
        return "mj/mjboard_detail";
    }

    // 수정 폼 (GET)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{mjSeq}")
    public String mjboardModify(Model model, MjboardForm mjboardForm, @PathVariable("mjSeq") Integer mjSeq) {
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        model.addAttribute("mjboard", mjboard);
        mjboardForm.setMjTitle(mjboard.getMjTitle());
        mjboardForm.setMjContent(mjboard.getMjContent());
        return "mj/mjboardModify_form";
    }

    // 게시글 수정 (POST)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{mjSeq}")
    public String mjboardModify(@Valid MjboardForm mjboardForm, BindingResult bindingResult, @PathVariable("mjSeq") Integer mjSeq) {
        if (bindingResult.hasErrors()) {
            return "mj/mjboardModify_form";
        }

        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        this.mjboardService.modify(mjboard, mjboardForm.getMjTitle(), mjboardForm.getMjContent());
        return "redirect:/mjboard/list";
    }

    // 게시글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{mjSeq}")
    public String delete(@PathVariable("mjSeq") Integer mjSeq) {
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        this.mjboardService.delete(mjboard);
        return "redirect:/mjboard/list";
    }

    // 추천 기능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mjRecommend/{mjSeq}")
    public String mjRecommend(@PathVariable("mjSeq") Integer mjSeq) {
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        this.mjboardService.mjRecommend(mjboard);
        return String.format("redirect:/mjboard/detail/%s", mjSeq);
    }
}
