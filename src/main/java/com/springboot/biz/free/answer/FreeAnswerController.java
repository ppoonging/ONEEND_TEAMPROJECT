package com.springboot.biz.free.answer;


import com.springboot.biz.free.board.FreeQuestion;
import com.springboot.biz.free.board.FreeQuestionRepository;
import com.springboot.biz.free.board.FreeQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freeanswer")
public class FreeAnswerController {

    private final FreeAnswerService freeAnswerService;
    private final FreeQuestionService freeQuestionService;





    @PostMapping("/create/{frboSeq}")
    public String createFreeAnswer(@PathVariable("frboSeq") Integer frboSeq,
                                   @Valid FreeAnswerForm freeAnswerForm,
                                   BindingResult bindingResult, Model model) {
        FreeQuestion freeQuestion = freeQuestionService.getFreeQuestion(frboSeq);

        if (bindingResult.hasErrors()) {
            model.addAttribute("freeQuestion", freeQuestion);
            return "free/freeQuestion_detail";
        }

        freeAnswerService.freeAnswerCreate(freeQuestion, freeAnswerForm.getFrboAnsContent(), null);

        return String.format("redirect:/freequestion/detail/%s", frboSeq);
    }



    @PostMapping("/modify/{frboAnSeq}")
    public String modifyFreeAnswer(@PathVariable("frboAnSeq") Integer frboAnSeq,
                                   @Valid FreeAnswerForm freeAnswerForm, BindingResult bindingResult) {

        // 유효성 검사 실패 시 원래 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "redirect:/freequestion/detail/" + freeAnswerService.getFreeAnswer(frboAnSeq).getFreeQuestion().getFrboSeq();
        }

        // 기존 댓글 가져오기
        FreeAnswer freeAnswer = freeAnswerService.getFreeAnswer(frboAnSeq);

        // 댓글 수정
        freeAnswerService.modify(freeAnswer, freeAnswerForm.getFrboAnsContent());

        // 수정 후 원래 질문 상세 페이지로 리디렉트
        return "redirect:/freequestion/detail/" + freeAnswer.getFreeQuestion().getFrboSeq();
    }


    @PostMapping("/delete/{frboAnSeq}")
    public String deleteFreeAnswer(@PathVariable("frboAnSeq") Integer frboAnSeq) {
        FreeAnswer freeAnswer = freeAnswerService.getFreeAnswer(frboAnSeq);
        Integer questionSeq = freeAnswer.getFreeQuestion().getFrboSeq(); // 질문 번호 가져오기

        freeAnswerService.delete(freeAnswer); // 댓글 삭제

        return "redirect:/freequestion/detail/" + questionSeq; // 질문 상세 페이지로 이동
    }

    @PostMapping("/reply/{frboAnSeq}")
    public String createReply(@PathVariable("frboAnSeq") Integer frboAnSeq,
                              @Valid FreeAnswerForm freeAnswerForm,
                              BindingResult bindingResult, Model model) {
        FreeAnswer parentAnswer = freeAnswerService.getFreeAnswer(frboAnSeq);
        FreeQuestion freeQuestion = parentAnswer.getFreeQuestion();

        if (bindingResult.hasErrors()) {
            model.addAttribute("freeQuestion", freeQuestion);
            return "free/freeQuestion_detail";
        }

        freeAnswerService.freeAnswerCreate(freeQuestion, freeAnswerForm.getFrboAnsContent(), parentAnswer);

        return String.format("redirect:/freequestion/detail/%s", freeQuestion.getFrboSeq());
    }









}