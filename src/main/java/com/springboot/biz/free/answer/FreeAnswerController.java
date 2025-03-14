package com.springboot.biz.free.answer;


import com.springboot.biz.free.board.FreeQuestion;
import com.springboot.biz.free.board.FreeQuestionRepository;
import com.springboot.biz.free.board.FreeQuestionService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freeanswer")
public class FreeAnswerController {

    private final FreeAnswerService freeAnswerService;
    private final FreeQuestionService freeQuestionService;
    private final HUserSerevice hUserSerevice;





    @PostMapping("/create/{frboSeq}")
    public String createFreeAnswer(@PathVariable("frboSeq") Integer frboSeq,
                                   @Valid FreeAnswerForm freeAnswerForm,
                                   BindingResult bindingResult, Model model , Principal principal) {
        FreeQuestion freeQuestion = freeQuestionService.getFreeQuestion(frboSeq);
        HUser hUser = hUserSerevice.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("freeQuestion", freeQuestion);
            return "free/freeQuestion_detail";
        }

        freeAnswerService.freeAnswerCreate(freeQuestion, freeAnswerForm.getFrboAnsContent(), null,hUser);

        return String.format("redirect:/freequestion/detail/%s", frboSeq);
    }






    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{frboAnSeq}")
    public String modifyFreeAnswer(@PathVariable("frboAnSeq") Integer frboAnSeq,
                                   @RequestParam("frboAnsContent") String frboAnsContent,
                                   Principal principal) {

        FreeAnswer freeAnswer = freeAnswerService.getFreeAnswer(frboAnSeq);

        if (!freeAnswer.getFrboAnsAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        // 서비스로 수정 위임
        freeAnswerService.modify(freeAnswer, frboAnsContent);

        // 원글로 이동
        return "redirect:/freequestion/detail/" + freeAnswer.getFreeQuestion().getFrboSeq();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{frboAnSeq}")
    public String deleteFreeAnswer(@PathVariable("frboAnSeq") Integer frboAnSeq, Principal principal) {
        FreeAnswer freeAnswer = freeAnswerService.getFreeAnswer(frboAnSeq);
        Integer questionSeq = freeAnswer.getFreeQuestion().getFrboSeq(); // 질문 번호 가져오기

        if(!freeAnswer.getFrboAnsAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다." );
        }

        freeAnswerService.delete(freeAnswer); // 댓글 삭제

        return "redirect:/freequestion/detail/" + questionSeq; // 질문 상세 페이지로 이동
    }


    @PostMapping("/reply/{frboAnSeq}")
    public String createReply(@PathVariable("frboAnSeq") Integer frboAnSeq,
                              @Valid FreeAnswerForm freeAnswerForm,
                              BindingResult bindingResult,Principal principal) {
        FreeAnswer parentAnswer = freeAnswerService.getFreeAnswer(frboAnSeq);
        FreeQuestion freeQuestion = parentAnswer.getFreeQuestion();
        HUser hUser = hUserSerevice.getUser(principal.getName());


        freeAnswerService.freeAnswerCreate(freeQuestion, freeAnswerForm.getFrboAnsContent(), parentAnswer,hUser);

        return String.format("redirect:/freequestion/detail/%s", freeQuestion.getFrboSeq());
    }









}