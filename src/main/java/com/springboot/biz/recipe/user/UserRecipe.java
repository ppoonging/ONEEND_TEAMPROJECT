package com.springboot.biz.recipe.user;

import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class UserRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userRecipeSeq; //유저레시피 기본키

    @ManyToOne
    private HUser userId; // 유저 번호 (FK)

    @Column(length = 100 )
    private String userRecipeTitle;  //유저 레시피 제목

    private String recipeCategory; //카테고리

    @Column(columnDefinition = "TEXT")
    private String userRecipeContent;  //유저 레시피 내용

    private LocalDateTime userRecipeRegDate;  //작성일

    private String userRecipeCnt;  //조회수

    private Integer recipeRecommend; //추천

    private LocalDateTime userRecipeModify; //레시피 수정일

    private String userRecipeFilePath;  //이미지

    private String userRecipeFileName;  //파일,이미지 이름

    @Column(columnDefinition = "TEXT")
    private String userrecipeIngre;  //재료 분량

    @Column(columnDefinition = "TEXT")
    private String userrecipeReady;  //준비하기







}
