package com.springboot.biz.recipe.user.board;

import com.springboot.biz.free.answer.FreeAnswer;
import com.springboot.biz.recipe.user.answer.UserRecipeAnswer;
import com.springboot.biz.user.MgUser;
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
    private MgUser userId; // 유저 번호 (FK)


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

    @OneToMany(mappedBy = "userRecipe", cascade = CascadeType.REMOVE)
    private List<UserRecipeAnswer> recipeAnswersList;





}
