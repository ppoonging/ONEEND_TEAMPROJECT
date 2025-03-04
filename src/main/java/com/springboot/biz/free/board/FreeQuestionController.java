package com.springboot.biz.free.board;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/freequestion")
public class FreeQuestionController {

    private final FreeQuestionService freeQuestionService;
    private final FreeThumbnailService freeThumbnailService;

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

    //글목록 조회
    @GetMapping("/list")
    public String freeQuestionList(Model model,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "kw", defaultValue = "") String kw,
                                   @RequestParam(value = "searchType", defaultValue = "both") String searchType) {

        // 검색 결과 가져오기
        Page<FreeQuestion> paging = freeQuestionService.getFreeQuestionList(page, kw, searchType);

        // 모델에 데이터 추가
        model.addAttribute("pageing", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("searchType", searchType);

        return "free/freequestion_list";  // 뷰 이름을 반환
    }


    //게시글 작성
          @GetMapping("/form")
  public String freeQuestionForm(FreeQuestionForm freeQuestionForm){
        return "free/freeQuestion_form";
  }


  @PostMapping("/form")
    public String freeQuestionForm(@Valid FreeQuestionForm freeQuestionForm , BindingResult bindingResult
     ,@RequestParam("file")MultipartFile file)throws Exception{

    if(bindingResult.hasErrors()){
        return "free/freeQuestion_form";
    }

     this.freeQuestionService.freeForm(freeQuestionForm.getFrboTitle(),freeQuestionForm.getFrboContent(),file);

    return "redirect:/freequestion/list";

   }

  @GetMapping("/detail/{frboSeq}")
   public String freeDetail(Model model, @PathVariable("frboSeq")Integer frboSeq){

        FreeQuestion freeQuestion = this.freeQuestionService.getFreeQuestion(frboSeq);
        model.addAttribute("freeQuestion",freeQuestion);
        return "free/freeQuestion_detail";

  }


  @GetMapping("/modify/{frboSeq}")
public String freeQuestionModify(FreeQuestionForm freeQuestionForm,@PathVariable("frboSeq") Integer frboSeq){

        FreeQuestion question = this.freeQuestionService.getFreeQuestion(frboSeq);

        freeQuestionForm.setFrboTitle(question.getFrboTitle());
        freeQuestionForm.setFrboContent(question.getFrboContent());

        return  "free/freeQuestion_form";
  }

  @PostMapping("/modify/{frboSeq}")
    public String freeQuestionModify(@Valid FreeQuestionForm freeQuestionForm,BindingResult bindingResult
  ,@PathVariable("frboSeq")Integer frboSeq){

        if(bindingResult.hasErrors()){
            return  "free/freeQuestion_form";
        }

        FreeQuestion question = this.freeQuestionService.getFreeQuestion(frboSeq);

        this.freeQuestionService.modify(question,freeQuestionForm.getFrboTitle(),freeQuestionForm.getFrboContent());

        return  String.format("redirect:/freequestion/detail/%s", frboSeq);

  }

@GetMapping("/delete/{frboSeq}")
public String questionDelete(@PathVariable("frboSeq")Integer frboSeq){
        FreeQuestion question = this.freeQuestionService.getFreeQuestion(frboSeq);

        this.freeQuestionService.delete(question);
        return "redirect:/freequestion/list";
}









}
