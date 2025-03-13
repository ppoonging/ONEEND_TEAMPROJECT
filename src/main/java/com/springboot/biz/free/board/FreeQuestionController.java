package com.springboot.biz.free.board;



import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/freequestion")
public class FreeQuestionController {

    private final FreeQuestionService freeQuestionService;
    private final FreeThumbnailService freeThumbnailService;
    private final HUserSerevice hUserSerevice;

    //자유게시판 view
    /*@GetMapping("/list")
    public String freeQuestion(Model model, @RequestParam(value = "page",
            defaultValue = "0") int page) {
        Page<FreeQuestion> paging = this.freeQuestionService.getFreeQuestionList(page);
        model.addAttribute("pageing", paging);
        return "freequestion_list";

    }*/
 /*   @GetMapping
    public String freeQuestionList(Model model, @RequestParam(value = "page",defaultValue = "0") int page) {
        Page<FreeQuestion> paging = this.freeQuestionService.getFreeQuestionList(page);
        model.addAttribute("pageing", paging);
        return "freequestionList_From";
    }*/

    @GetMapping("/")
    public String freeQuestionList(Model model,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "kw", defaultValue = "") String kw,
                                   @RequestParam(value = "searchType", defaultValue = "both") String searchType,
                                   @RequestParam(value = "sort", defaultValue = "recent") String sort) {

        Page<FreeQuestion> paging;

        // 정렬 조건에 따라 서비스 호출
        if ("popular".equals(sort)) {
            paging = freeQuestionService.getPopularQuestionList(page, kw, searchType); // 추천순
        } else {
            paging = freeQuestionService.getFreeQuestionList(page, kw, searchType); // 최신순 (기본)
        }

        model.addAttribute("pageing", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("searchType", searchType);
        model.addAttribute("sort", sort); // 현재 정렬 방식 뷰에 넘기기

        return "free/freequestion_list";
    }






    //게시글 작성

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/form")
  public String freeQuestionForm(FreeQuestionForm freeQuestionForm){

        return "free/freeQuestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/form")
    public String freeQuestionForm(@Valid FreeQuestionForm freeQuestionForm , BindingResult bindingResult
            , @RequestParam("file")MultipartFile file, Principal principal)throws Exception{

        if(bindingResult.hasErrors()){
            return "free/freeQuestion_form";
        }

        HUser hUser = this.hUserSerevice.getUser(principal.getName());


        this.freeQuestionService.freeForm(freeQuestionForm.getFrboTitle(),freeQuestionForm.getFrboContent(),file,hUser);

        return "redirect:/freequestion/";

    }

    @GetMapping("/detail/{frboSeq}")
    public String freeDetail(Model model, @PathVariable("frboSeq")Integer frboSeq){

        FreeQuestion freeQuestion = this.freeQuestionService.getFreeQuestion(frboSeq);
        model.addAttribute("freeQuestion",freeQuestion);
        return "free/freeQuestion_detail";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{frboSeq}")
    public String freeQuestionModify(FreeQuestionForm freeQuestionForm,@PathVariable("frboSeq") Integer frboSeq,Principal principal){

        FreeQuestion question = this.freeQuestionService.getFreeQuestion(frboSeq);
        if(!question.getFreeAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
        }

        freeQuestionForm.setFrboTitle(question.getFrboTitle());
        freeQuestionForm.setFrboContent(question.getFrboContent());

        return  "free/freeQuestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{frboSeq}")
    public String freeQuestionModify(@Valid FreeQuestionForm freeQuestionForm,BindingResult bindingResult
            ,@PathVariable("frboSeq")Integer frboSeq,Principal principal){

        if(bindingResult.hasErrors()){
            return  "free/freeQuestion_form";
        }

        FreeQuestion question = this.freeQuestionService.getFreeQuestion(frboSeq);

        if(!question.getFreeAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
        }

        this.freeQuestionService.modify(question,freeQuestionForm.getFrboTitle(),freeQuestionForm.getFrboContent());

        return  String.format("redirect:/freequestion/detail/%s", frboSeq);

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{frboSeq}")
    public String questionDelete(@PathVariable("frboSeq")Integer frboSeq,Principal principal){
        FreeQuestion question = this.freeQuestionService.getFreeQuestion(frboSeq);

        if(!question.getFreeAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다");
        }

        this.freeQuestionService.delete(question);
        return "redirect:/freequestion/";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recommend/{frboSeq}")
    public String toggleQuestionRecommend(Principal principal, @PathVariable("frboSeq") Integer frboSeq) {
        FreeQuestion freeQuestion = this.freeQuestionService.getFreeQuestion(frboSeq);
        HUser hUser = this.hUserSerevice.getUser(principal.getName()); // 로그인한 사용자
        this.freeQuestionService.toggleRecommend(freeQuestion, hUser); // 추천/취소
        return String.format("redirect:/freequestion/detail/%s", frboSeq); // 상세로 리다이렉트
    }







}
