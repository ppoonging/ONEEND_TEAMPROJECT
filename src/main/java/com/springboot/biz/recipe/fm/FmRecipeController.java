package com.springboot.biz.recipe.fm;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;


@Controller
@RequiredArgsConstructor

@RequestMapping("/fm")
public class FmRecipeController {

    private final FmRecipeService fmRecipeService;
    private final HUserSerevice mgUserSerevice;


    @GetMapping("/recipe")
    public String list(Model model, @RequestParam(value = "kw", defaultValue = "") String kw
            , @RequestParam(value = "page",defaultValue = "0") int page) {
        Page<FmRecipe> paging =  this.fmRecipeService.getList(page,kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "fm/fmRecipeList";
    }


        @GetMapping("/create")
        @PreAuthorize("isAuthenticated()")
        public String create() {
            return "fm/fmRecipeCreate";
        }

        @PostMapping("/create")
        @PreAuthorize("isAuthenticated()")  // 로그인한 사용자만 접근 가능 (OAuth2 포함)
        public String questionCreate(Principal principal,FmRecipe fmRecipe,@RequestParam("file") MultipartFile file) throws IOException {

            HUser mgUser = this.mgUserSerevice.getUser(principal.getName());

            this.fmRecipeService.createRecipe(fmRecipe.getFmrecipeCategory(),
                    fmRecipe.getFmrecipeTitle(), fmRecipe.getFmrecipeContent()
                    , file);
            return "redirect:/fm/fmRecipeList";
        }



    }