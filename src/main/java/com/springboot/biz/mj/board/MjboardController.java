package com.springboot.biz.mj.board;

import com.springboot.biz.mj.answer.MjAnswerForm;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mjboard")
public class MjboardController {

    private final HUserSerevice hUserSerevice;
    private final MjboardService mjboardService;

    // 목록
    @GetMapping("/list")
    public String list(Model model,
                       @PageableDefault(size = 6, sort = "mjRegDate", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {

        Map<String, Object> result = mjboardService.getList(pageable, kw);
        model.addAttribute("paging", result.get("paging"));
        model.addAttribute("starCountMap", result.get("starCountMap"));
        model.addAttribute("kw", kw);
        return "mj/mjboard_list";
    }

    // 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(MjboardForm mjboardForm) {
        return "mj/mjboard_form";
    }

    // 저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createPost(@Valid MjboardForm mjboardForm,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file,
                             Principal principal) throws Exception {
        if (bindingResult.hasErrors()) {
            return "mj/mjboard_form";
        }
        HUser hUser = hUserSerevice.getUser(principal.getName());
        mjboardService.create(mjboardForm.getMjTitle(), mjboardForm.getMjContent(), file, hUser, 0);
        return "redirect:/mjboard/list";
    }

    // 상세
    @GetMapping("/detail/{mjSeq}")
    public String detail(Model model, @PathVariable("mjSeq") Integer mjSeq) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        model.addAttribute("mjanswerForm", new MjAnswerForm());
        model.addAttribute("mjreplyForm", new MjAnswerForm());
        model.addAttribute("mjboard", mjboard);
        return "mj/mjboard_detail";
    }

    // 썸머노트 이미지 업로드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String imageUrl = mjboardService.saveSummernoteImage(file);
        return ResponseEntity.ok(Collections.singletonMap("url", imageUrl));
    }

    // 외부 이미지 업로드 (수정 X, 네가 준 코드 그대로, 다만 import 필요)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadExternalImage")
    @ResponseBody
    public Map<String, String> uploadExternalImage(@RequestBody Map<String, String> req) throws Exception {
        String imageUrl = req.get("url");
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";

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
        return Collections.singletonMap("url", "/files/mj/" + fileName);
    }

    // 추천 (리다이렉트 방식 그대로 유지, 네가 준 코드 그대로)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mjRecommend/{mjSeq}")
    public String recommend(@PathVariable("mjSeq") Integer mjSeq, Principal principal) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        HUser user = hUserSerevice.getUser(principal.getName());
        mjboardService.mjRecommend(mjboard, user);
        return "redirect:/mjboard/detail/" + mjSeq;
    }

    // 수정 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{mjSeq}")
    public String modifyForm(Model model, MjboardForm form, @PathVariable Integer mjSeq) {
        Mjboard board = mjboardService.getMjboard(mjSeq);
        form.setMjTitle(board.getMjTitle());
        form.setMjContent(board.getMjContent());
        model.addAttribute("mjboard", board);
        return "mj/mjboardModify_form";
    }

    // 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{mjSeq}")
    public String modify(@Valid MjboardForm form, BindingResult bindingResult, @PathVariable Integer mjSeq) {
        if (bindingResult.hasErrors()) return "mj/mjboardModify_form";
        Mjboard board = mjboardService.getMjboard(mjSeq);
        mjboardService.modify(board, form.getMjTitle(), form.getMjContent());
        return "redirect:/mjboard/list";
    }

    // 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{mjSeq}")
    public String delete(@PathVariable Integer mjSeq) {
        Mjboard board = mjboardService.getMjboard(mjSeq);
        mjboardService.delete(board);
        return "redirect:/mjboard/list";
    }
}
