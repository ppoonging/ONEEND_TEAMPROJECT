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
    private Integer userRecipeSeq;

    @Column(length = 100 )
    private String userRecipeTitle;

    @Column(columnDefinition = "TEXT")
    private String userRecipeContent;

    private String userRecipeRegDate;

    private String userRecipeCnt;

    private String userRecipeImage;

    private Integer userSeq; // 유저 번호 (FK)




}
