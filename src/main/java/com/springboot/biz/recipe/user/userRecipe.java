package com.springboot.biz.recipe.user;

import com.sun.jdi.event.StepEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class userRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userRecipeSeq; //유저레시피 기본치

    @Column(length = 100 )
    private String userRecipeTitle;  //유저 레시피 제목

    @Column(columnDefinition = "TEXT")
    private String userRecipeContent;  //유저 레시피 내용

    private String userRecipeRegDate;  //작성일

    private String userRecipeCnt;  //추천수

    private String userRecipeImage;  //이미지

    private Integer userSeq; // 유저 번호 (FK)




}
