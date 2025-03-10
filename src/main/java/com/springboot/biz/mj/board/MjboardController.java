package com.springboot.biz.mj.board;


import com.springboot.biz.mj.answer.MjAnswerForm;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mjboard")
public class MjboardController {
    private  final HUserSerevice hUserSerevice;
    private final MjboardService mjboardService;


    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page) {
        Page<Mjboard> paging = this.mjboardService.getList(page);
        model.addAttribute("paging", paging);
        return "mj/mjboard_list";
    }
    //게시글 작성 폼으로 이동
    @GetMapping("/create")
    public String mjboardCreate(MjboardForm mjboardForm){
        return "mj/mjboard_form";
    }

    //질문저장
    @PostMapping("/create")
    public String mjboaordCreate(@RequestParam(value = "mjTitle")String mjTitle,
                                 @RequestParam(value = "mjContent")String mjContent,
                                 @RequestParam(value = "file",required = false)MultipartFile file){
        try {
            mjboardService.create(mjTitle, mjContent, file);
        }catch(IOException e){
            e.printStackTrace();
            return  "error"; //파일 업로드 실패 시 오류 페이지로 리디렉션
        }
        return "redirect:/mjboard/list";
    }
    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 파일 저장 후 URL 반환
            String imageUrl = mjboardService.saveFile(file);

            // 썸머노트가 이해할 수 있도록 JSON 응답을 올바르게 수정
            Map<String, Object> response = new HashMap<>();
            response.put("url", imageUrl);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "이미지 업로드 실패"));
        }
    }


    //상세 페이지를 요청하면 실행되는 컨트롤 메서드 (상세페이지로 전달하는 역할)
    @GetMapping("/detail/{mjSeq}") //URL을 매핑( 요청받는 역할)
    public String detail(Model model, @PathVariable("mjSeq") Integer mjSeq, MjAnswerForm mjAnswerForm){
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        model.addAttribute("mjboard", mjboard);
        return "mj/mjboard_detail";
    }
    @PostMapping("/modify/{mjSeq}")
    //@PreAuthorize("isAuthenticated()") 유저 로그인 하면 살리기
    public String mjboardModify(@Valid MjboardForm mjboardForm, BindingResult bindingResult
                               /*Principal principal 로그인 하면 살리기*/, @PathVariable("mjSeq")Integer mjSeq){
        if(bindingResult.hasErrors()){
            return "mj/mjboard_list";
        }
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);{
            //유저 로그인 하면 추가하기.
        }
       this.mjboardService.modify(mjboard, mjboardForm.getMjTitle(),mjboardForm.getMjContent());
        return String.format("redirect:/mjboard/list");
    }
    @GetMapping("/modify/{mjSeq}")
    public String mjboardModify(Model model,MjboardForm mjboardForm, @PathVariable("mjSeq")Integer mjSeq){
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        model.addAttribute("mjboard", mjboard);
        mjboardForm.setMjTitle(mjboard.getMjTitle());
        mjboardForm.setMjContent(mjboard.getMjContent());
        return "mj/mjboardModify_form";
    }
    @GetMapping("/delete/{mjSeq}")
    public String delete(@PathVariable("mjSeq")Integer mjSeq){
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);

        this.mjboardService.delete(mjboard);
        return "redirect:/mjboard/list";
    }
    @GetMapping("/mjRecommend/{mjSeq}")
    public String mjRecommend(@PathVariable("mjSeq") Integer mjSeq){
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        this.mjboardService.mjRecommend(mjboard);
        return String.format("redirect:/mjboard/detail/%s",mjSeq);

    }
}
