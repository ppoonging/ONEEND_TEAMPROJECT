package com.springboot.biz.mj.answer;

import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.mj.board.MjboardService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mjanswer")
public class MjAnsController {

    private final MjboardService mjboardService;
    private final HUserSerevice hUserSerevice;
    private final MjAnsService mjAnsService;


    // 댓글 등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{mjSeq}")
    public String createAnswer(@PathVariable("mjSeq") Integer mjSeq,
                               @Valid MjAnswerForm mjAnswerForm,
                               BindingResult bindingResult,
                               Principal principal) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        HUser user = hUserSerevice.getUser(principal.getName()); // 로그인 유저
        mjAnsService.createMjAnswer(mjboard, mjAnswerForm.getMjAnsContent(), user);
        return String.format("redirect:/mjboard/detail/%s", mjSeq);
    }

    // 대댓글 등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reply/{mjAnsSeq}")
    public String createReply(@PathVariable("mjAnsSeq") Integer mjAnsSeq,
                              @Valid MjAnswerForm mjAnswerForm,
                              BindingResult bindingResult,
                              Principal principal) {
        MjAnswer parentAnswer = mjAnsService.getMjAnswer(mjAnsSeq);
        Mjboard mjboard = parentAnswer.getMjBoard();
        HUser user = hUserSerevice.getUser(principal.getName()); // 로그인 유저
        mjAnsService.createMjAnswer(mjboard, mjAnswerForm.getMjAnsContent(), parentAnswer, user);
        return String.format("redirect:/mjboard/detail/%s", mjboard.getMjSeq());
    }



    @GetMapping("/modify/{mjSeq}")
    public String mjAnsModify(MjAnswerForm mjAnswerForm, @PathVariable("mjSeq") Integer mjSeq) {
        MjAnswer mjAnswer = this.mjAnsService.getMjAnswer(mjSeq);{
        }
        mjAnswerForm.setMjAnsContent(mjAnswer.getMjAnsContent());
        return "mj/mjanswerModify_form";
    }
    @GetMapping("delete/{mjSeq}")
    public String mjAnsDelete(@PathVariable("mjSeq") Integer mjSeq) {
        MjAnswer mjAnswer = this.mjAnsService.getMjAnswer(mjSeq);
        this.mjAnsService.deleteMjAnswer(mjAnswer);
        return String.format("redirect:/mjboard/detail/%s", mjSeq);
    }
    @GetMapping("/mjAnsRecommend/{mjSeq}")
    public String mjAnsRecommend(@PathVariable("mjSeq") Integer mjSeq) {
        System.out.println("추천 요청: mjanswer.mjSeq = " + mjSeq);
        MjAnswer mjAnswer = this.mjAnsService.getMjAnswer(mjSeq);
        //추천수 증가

        int currentRecommend = mjAnswer.getMjAnsRecommend();
        mjAnswer.setMjAnsRecommend(currentRecommend + 1);


        this.mjAnsService.mjAnsRecommend(mjAnswer);

        return String.format("redirect:/mjboard/detail/%s", mjSeq);

    }

}
