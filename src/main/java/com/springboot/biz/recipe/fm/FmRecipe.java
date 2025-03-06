package com.springboot.biz.recipe.fm;


import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class FmRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fmrecipeSeq;   //fm레시피 (PK)


    @ManyToOne
    private HUser userId;  //유저번호(FK)

    private String fmrecipeCategory; //카테고리 설정

    @Column(length = 100)
    private String fmrecipeTitle;  //fm레시피 음식이름

    @Column(columnDefinition = "TEXT")
    private String fmrecipeIngre;  //재료 분량

    @Column(columnDefinition = "TEXT")
    private String fmrecipeReady;  //준비하기

    @Column(columnDefinition = "TEXT")
    private String fmrecipeContent;   //fm레시피 레시피



    private LocalDateTime fmrecipeRegDate;  //작성일

    @Column(columnDefinition = "Integer default 0")
    @NonNull
    private Integer fmrecipeCnt;   //추천수

    private String fmrecipeFilePath;  //이미지,파일

    private String fmrecipeFileName;  //이미지 파일 이름



}
