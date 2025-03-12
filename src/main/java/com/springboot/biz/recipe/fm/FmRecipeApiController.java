package com.springboot.biz.recipe.fm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fm")
public class FmRecipeApiController {

    private final FmRecipeService fmRecipeService;
    private final FmRecipeRepository fmRecipeRepository;


    /*마이페이지에 모달로 즐찾 레시피보기*/
    @GetMapping("/api/detail/{id}")
    public Map<String, Object> getRecipeDetail(@PathVariable Integer id) {
        FmRecipe recipe = fmRecipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피가 존재하지 않습니다."));

        Map<String, Object> result = new HashMap<>();
        result.put("title", recipe.getFmrecipeTitle());
        result.put("content", recipe.getFmrecipeContent());
        result.put("ingredients", recipe.getFmrecipeIngre());
        result.put("ready", recipe.getFmrecipeReady());
        result.put("image", recipe.getFmrecipeFilePath());
        return result; // JSON 형태로 반환
    }
}
