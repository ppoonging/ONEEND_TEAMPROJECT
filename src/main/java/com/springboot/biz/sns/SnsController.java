package com.springboot.biz.sns;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SnsController {

    private final SnsService snsService;

    @GetMapping("/sns")
    public String sns(Model model) {

        List<Sns> snsList = this.snsService.getAll();
        model.addAttribute("snsList", snsList);
        return "main/sns/sns_main";
    }

    @PostMapping("/upload")
    public String registerSns(@RequestParam("file") MultipartFile file, @RequestParam("link") String link) throws IOException {
        this.snsService.save(file, link);

        return "redirect:/sns";
    }
}
