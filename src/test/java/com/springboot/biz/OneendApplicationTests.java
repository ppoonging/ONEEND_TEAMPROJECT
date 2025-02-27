package com.springboot.biz;


import com.springboot.biz.free.board.FreeQuestionRepository;
import com.springboot.biz.recipe.fm.FmRecipe;
import com.springboot.biz.recipe.fm.FmRecipeRepository;
import com.springboot.biz.recipe.fm.FmRecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class OneendApplicationTests {



    @Autowired
    private FreeQuestionRepository freeQuestionRepository;
    @Autowired
    private FmRecipeService fmRecipeService;
    @Autowired
    private FmRecipeRepository fmRecipeRepository;



    @Test
    void contextLoads() {
/*
        for(int i =0; i<10; i++){

            FmRecipe fm = new FmRecipe();
            fm.setFmrecipeTitle("삼겹살2");
            fm.setFmrecipeContent("삼겹살은 마싯지2");
            fm.setFmrecipeRegDate(LocalDateTime.now());
            this.fmRecipeRepository.save(fm);*/
        }



        /*FreeQuestion f = new FreeQuestion();
        f.setFrboTitle("테스트 제목입니다");
        f.setFrboContent("테스트 내용입니다");
        f.setFrboRegDate(LocalDateTime.now());
        this.freeQuestionRepository.save(f);
*/




}
