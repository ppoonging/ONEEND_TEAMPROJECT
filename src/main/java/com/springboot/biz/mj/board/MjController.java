package com.springboot.biz.mj.board;
import com.springboot.biz.mj.answer.MjAnswerForm;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mjboard")
@EnableMethodSecurity(prePostEnabled = true)
public class MjController {

    private final HUserSerevice hUserSerevice;
    private final MjService mjService;


    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Mjboard> pageing = this.mjService.getList(page, kw);
        model.addAttribute("pageing", pageing);
        model.addAttribute("kw", kw);
        return "mj/mjboard_list";

    }

    @GetMapping("/mjform")
    public String mjRegdate(MjForm mjForm, Model model) {
        model.addAttribute("mjForm", mjForm);
        return "mj/mjboard_form";
    }


    @PreAuthorize("isAuthenticated()")
     @PostMapping("/mjregdate")
     public  String mjboardregdeate(@Valid MjForm mjForm, BindingResult bindingResult, Principal principal) {
         if (bindingResult.hasErrors()) {
             return "mj/mjboard_form";
         }
         HUser hUser = this.hUserSerevice.getUser(principal.getName());
         this.mjService.mjregdate(mjForm.getMjTitle(), mjForm.getMjContent(),hUser);
         return "redirect:/mjboard/list";

     }

    @GetMapping("/detail/{mjseq}")
    public String mjdetail(Model model, @PathVariable("mjseq") Integer mjseq, MjAnswerForm mjAnswerForm) {
        Mjboard mjboard = this.mjService.getMjboard(mjseq);
        model.addAttribute("mjboard", mjboard);
        return "mj/mjboard_detail";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{mjseq}")
    public String mjboardmodify(@Valid MjForm mjForm, BindingResult bindingResult,
                                Principal principal, @PathVariable("mjseq") Integer mjseq) {
        if (bindingResult.hasErrors()) {
            return "mj/mjboard_form";
        }
        Mjboard mjboard = this.mjService.getMjboard(mjseq);
        // 유저 권한 받으면 수정 권한 넣어야함
        if (!mjboard.getMjTitle().equals(mjForm.getMjTitle())) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.mjService.modify(mjboard, mjForm.getMjTitle(), mjForm.getMjContent());
        return String.format("redirect:/mjboard/detail/%s", mjseq);


    }

}





