package com.springboot.biz.notion;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notion")
public class MgController {

    private final MgService mgService;

    @GetMapping("/")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<MgNotion> paging = this.mgService.getList(page);
        model.addAttribute("paging", paging);
        return "notionList";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("notionSeq") Integer notionSeq) {
        MgNotion mgNotion = this.mgService.getMgNotion(notionSeq);
        model.addAttribute("mgNotion", mgNotion);
        return "notionDetail";
    }


    @GetMapping("/create")
    public String Create(MgNotionForm mgNotionForm) {
        return "notionForm";
    }




}

