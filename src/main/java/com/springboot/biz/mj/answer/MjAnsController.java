package com.springboot.biz.mj.answer;


import com.springboot.biz.mj.board.MjForm;
import com.springboot.biz.mj.board.MjService;
import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class MjAnsController {

    private final MjService mjService;
    private final MjAnsService mjAnsService;
    private final HUserSerevice hUserSerevice;

    @PostMapping("/mjregdate/{mjAnsSeq}")
    public String mjregdate(Model model, @PathVariable("mjAnsSeq") Integer mjAnsSeq,
                            @Valid MjAnswerForm mjAnswerForm, BindingResult bindingResult, Principal principal) {
        Mjboard mjboard = this.mjService.getMjboard(mjAnsSeq);
        HUser hUser = this.hUserSerevice.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("mjboard", mjboard);
            return "mj/mjboard_detail";
        }
        MjAnswer mjAnswer = this.mjAnsService.mjregdateAns(mjboard, mjAnswerForm.getContent(), hUser);
        return String.format("redirect:/mjboard/detail/%s#mjanswer_%s", mjAnswer.getMjBoard().getUserId(), mjAnswer.getUserId());
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{mjAnsSeq}")
    public String mjansModify(@Valid MjAnswerForm mjAnswerForm, BindingResult bindingResult, Principal principal
    , @PathVariable("mjAnsSeq") Integer mjAnsSeq) {
        if (bindingResult.hasErrors()) {
            return "mj/mjboard_form";
        }
        MjAnswer mjAnswer = this.mjAnsService.getMjAnswer(mjAnsSeq);
        if (!mjAnswer.getUserId().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }
        this.mjAnsService.modify(mjAnswer, mjAnswerForm.getContent());
        return String.format("redirect:/mjboard/detaile", mjAnswer.getMjBoard().getUserId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify{mjSeq}")
    public String mjmodify(MjForm mjForm, @PathVariable("mjSeq") Integer mjSeq, Principal principal) {
        Mjboard mjboard = this.mjService.getMjboard(mjSeq);
        if (!mjboard.getUserId().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한 없음");
        }
        mjForm.setMjTitle(mjboard.getMjTitle());
        mjForm.setMjContent(mjboard.getMjContent());
        return "mj/mjboard_form";
    }






}
