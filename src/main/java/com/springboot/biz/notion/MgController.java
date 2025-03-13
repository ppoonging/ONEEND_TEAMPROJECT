package com.springboot.biz.notion;

import com.springboot.biz.user.HUserSerevice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notion")
public class MgController {

    private final MgService mgService;
    private final HUserSerevice hUserSerevice;

    @GetMapping("/")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<MgNotion> paging = this.mgService.getList(page);
        model.addAttribute("paging", paging);
        return "notionList";
    }

    @GetMapping("/detail/{notionSeq}")
    public String detail(Model model, @PathVariable("notionSeq") Integer notionSeq) {
        MgNotion mgNotion = this.mgService.getMgNotion(notionSeq);
        model.addAttribute("mgNotion", mgNotion);
        return "notionDetail";
    }


    @GetMapping("/create")
    public String Create(MgNotionForm mgNotionForm) {
        return "notionForm";
    }



    @PostMapping("/create")
    public String Create(@Valid MgNotionForm mgNotionForm, BindingResult bindingResult, @RequestParam("file") MultipartFile file) throws Exception {
        // Check for validation errors
        if(bindingResult.hasErrors()) {
            return "notionForm";  // Return back to the form page
        }

        // Call service to handle creating the notion and save the file41616156
        this.mgService.create(mgNotionForm.getNotionTitle(), mgNotionForm.getNotionContent(), file);

        // Redirect to the notion listing page after success15615615
        return "redirect:/notion/";
    }

    @GetMapping("/modify/{notionSeq}")
    public String Modify(MgNotionForm mgNotionForm, @PathVariable("notionSeq") Integer notionSeq) {
        MgNotion mgNotion =this.mgService.getMgNotion(notionSeq);

        mgNotionForm.setNotionTitle(mgNotion.getNotionTitle());
        mgNotionForm.setNotionContent(mgNotion.getNotionContent());
        return "notionForm";
    }

    @PostMapping("/modify/{notionSeq}")
    public String Modify(@Valid MgNotionForm mgNotionForm, BindingResult bindingResult, @PathVariable("notionSeq") Integer notionSeq, @RequestParam("file") MultipartFile file)throws Exception {
        if(bindingResult.hasErrors()) {
            return "notionForm";
        }
        MgNotion mgNotion = this.mgService.getMgNotion(notionSeq);

        this.mgService.modify(mgNotion, mgNotionForm.getNotionTitle(), mgNotionForm.getNotionContent(), file);
        return String.format("redirect:/notion/detail/%s", notionSeq);
    }


    @GetMapping("/delete/{notionSeq}")
    public String Delete(@PathVariable("notionSeq") Integer notionSeq) {
        MgNotion mgNotion = this.mgService.getMgNotion(notionSeq);

        this.mgService.delete(mgNotion);
        return "redirect:/notion/";
    }



}

