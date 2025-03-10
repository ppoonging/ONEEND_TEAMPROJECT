package com.springboot.biz.mj.answer;

import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.mj.board.MjboardService;
import com.springboot.biz.user.HUserSerevice;
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
@RequestMapping("/mjanswer")
public class MjAnsController {

    private final MjboardService mjboardService;
    private final HUserSerevice hUserSerevice;
    private final MjAnsService mjAnsService;


    @PostMapping("/create/{mjSeq}")
    public String createAnswer(Model model, @PathVariable("mjSeq") Integer mjSeq,
                               @Valid MjAnswerForm mjAnswerForm, BindingResult bindingResult) {
        Mjboard mjboard = this.mjboardService.getMjboard(mjSeq);
        System.out.println("xtx" + mjAnswerForm.getMjAnsContent()); //폼에 저장된 값을 확인.(데이터가 들어오는지 확인)
        this.mjAnsService.createMjAnswer(mjboard, mjAnswerForm.getMjAnsContent()); //답변을 저장
        return  String.format("redirect:/mjboard/detail/%s", mjSeq);
    }
    @GetMapping("/modify/{mjSeq}")
    public String mjAnsModify(MjAnswerForm mjAnswerForm, @PathVariable("mjSeq") Integer mjSeq) {
        MjAnswer mjAnswer = this.mjAnsService.getMjAnswer(mjSeq);{
        }
        mjAnswerForm.setMjAnsContent(mjAnswer.getMjAnsContent());
        return "mj/mjanswerModify_form";
    }
    @GetMapping("delate/{mjSeq}")
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
