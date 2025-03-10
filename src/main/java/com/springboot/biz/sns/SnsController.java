package com.springboot.biz.sns;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sns")
public class SnsController {

    private final SnsService snsService;
    private final HUserSerevice hUserSerevice;

    @GetMapping("")
    public String sns(Model model) {

        List<Sns> snsList = this.snsService.getAll();
        model.addAttribute("snsList", snsList);
        return "main/sns/sns_main";
    }

    @GetMapping("/form")
    public String create(){
        return "main/sns/sns_form";
    }

    @PostMapping("/form/save")
    public String create(@RequestParam("file") MultipartFile file, @RequestParam("link") String link, @RequestParam("tag") String tag, Principal principal) throws IOException {

        HUser user = this.hUserSerevice.getUser(principal.getName());

        this.snsService.save(file, link, tag, user);

        return "redirect:/sns";
    }
}
