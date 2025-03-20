package com.springboot.biz.random;

import com.springboot.biz.recipe.fm.FmRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/random")
@RequiredArgsConstructor
public class RandomController {

    private final RandomService randomService;

    @GetMapping("/recipes")
    public List<FmRecipe> getRandomRecipeList() {
        return randomService.getRandomRecipeList(); // JSON 형태로 반환
    }
}
