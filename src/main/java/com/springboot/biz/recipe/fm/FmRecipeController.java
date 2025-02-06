package com.springboot.biz.recipe.fm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class FmRecipeController {

    private final FmRecipeService fmRecipeService;

    @GetMapping("/fm")
    public String FmRecipePage(){
        return "FmRecipe";

    }
}
