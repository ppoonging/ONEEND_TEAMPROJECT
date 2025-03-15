package com.springboot.biz.recipe.user.answer;


import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserRecipeAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  uRecipeAnsSeq;

    @ManyToOne
    private HUser userId; //유저 아이디 (FK)

    private Integer uRecipeSeq; //유저 레시피 (FK)

    private String uRecipeAnsContent; //레시피 답변 내용

    private LocalDateTime uRecipeAnsRegDate; //레시피 답변 작성일

    private LocalDateTime uRecipeAnsModifyDate;  //레시피 답변 수정일

    private Integer uRecipeAnsRecommend; //레시피 답변 추천

    private String uRecipeAnsComment; //레시피 답변에 답변



}
