package com.springboot.biz.sns;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
        return "sns/sns_main";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/form")
    public String create(SnsDTO snsDTO){
        return "sns/sns_form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/form/save")
    public String create(@RequestParam("file") MultipartFile file, @RequestParam("link") String link, @RequestParam("tag") String tag,
                         @RequestParam("title") String title, @RequestParam("content") String content,
                         Principal principal) throws IOException {

        HUser user = this.hUserSerevice.getUser(principal.getName());

        this.snsService.save(file, link, title, content, tag, user);

        return "redirect:/sns";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/form/modify/{snsSeq}")
    public String modify(@PathVariable("snsSeq") Integer snsSeq, SnsDTO snsDTO, Principal principal, Model model){

        HUser user = this.hUserSerevice.getUser(principal.getName());

        Sns sns = this.snsService.get(snsSeq);

        if(!sns.getUserId().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        snsDTO.setTitle(sns.getSnsCommentTitle());
        snsDTO.setContent(sns.getSnsCommentContent());
        snsDTO.setTag(sns.getSnsTag());
        snsDTO.setLink(sns.getSnsLink());
        snsDTO.setImagePath(sns.getSnsImagePath());
        snsDTO.setImageName(sns.getSnsImageName());

        model.addAttribute("snsSeq", snsSeq);
        model.addAttribute("sns", sns);

        return "sns/sns_form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/form/modify/{snsSeq}")
    public String modify(@PathVariable("snsSeq") Integer snsSeq, SnsDTO snsDTO,
                         Principal principal) throws IOException {

        HUser user = this.hUserSerevice.getUser(principal.getName());

        Sns sns = this.snsService.get(snsSeq);

        if (!sns.getUserId().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        this.snsService.modify(snsSeq, snsDTO.getFile(), snsDTO.getImagePath(), snsDTO.getLink(), snsDTO.getTitle(), snsDTO.getContent(), snsDTO.getTag(), user);

        return "redirect:/sns";
    }
}
