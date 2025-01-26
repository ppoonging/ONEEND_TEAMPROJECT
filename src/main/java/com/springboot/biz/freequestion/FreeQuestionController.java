package com.springboot.biz.freequestion;



import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/freequestion")
public class FreeQuestionController {

    private final FreeQuestionService freeQuestionService;

    //자유게시판 view
    @GetMapping("/list")
    public String freeQuestion(Model model, @RequestParam(value = "page",
            defaultValue = "0") int page) {
        Page<FreeQuestion> paging = this.freeQuestionService.getFreeQuestionList(page);
        model.addAttribute("pageing", paging);
        return "freequestion_list";

    }
 /*   @GetMapping
    public String freeQuestionList(Model model, @RequestParam(value = "page",defaultValue = "0") int page) {
        Page<FreeQuestion> paging = this.freeQuestionService.getFreeQuestionList(page);
        model.addAttribute("pageing", paging);
        return "freequestionList_From";
    }*/
}
