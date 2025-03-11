package com.springboot.biz.mj.board;
import com.mysql.cj.MysqlConnection;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mjboard")
@EnableMethodSecurity(prePostEnabled = true)
public class MjboardController {

    private final HUserSerevice hUserSerevice;
    private final MjboardService mjboardService;

    @PostMapping("/uploadExternalImage")
    @ResponseBody
    public Map<String, String> uploadExternalImage(@RequestBody Map<String, String> req) throws Exception {
        String imageUrl = req.get("url");
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";

        // 이미지 다운로드
        URL url = new URL(imageUrl);
        String fileName = UUID.randomUUID() + "_" + Paths.get(url.getPath()).getFileName().toString();
        File file = new File(projectPath, fileName);

        try (InputStream in = url.openStream(); OutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        // 저장된 이미지 경로 반환
        return Collections.singletonMap("url", "/files/mj/" + fileName);
    }
    /*  // 게시판 리스트
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Mjboard> paging = this.mjboardService.getList(page);
        model.addAttribute("paging", paging);
        return "mj/mjboard_list";
    }*/
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 9) Pageable pageable,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Mjboard> paging = mjboardService.getList(pageable, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "mj/mjboard_list"; // 경로 확인!
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
    // 추천 기능 (로그인 필요)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mjRecommend/{mjSeq}")
    @ResponseBody
    public Map<String, Integer> mjRecommend(@PathVariable("mjSeq") Integer mjSeq, Principal principal) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        HUser user = hUserSerevice.getUser(principal.getName());
        int recommendCount = mjboardService.mjRecommend(mjboard, user); // 추천/취소 서비스 메소드 호출
        return Map.of("count", recommendCount); // json 형태로 반환
    }
    /*@GetMapping("test")
    public String test() {
        return "mj/test";
    }*/
}
