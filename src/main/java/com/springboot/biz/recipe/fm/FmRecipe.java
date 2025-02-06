package com.springboot.biz.recipe.fm;


import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Entity
public class FmRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fmrecipeSeq;   //fm레시피 (PK)



    @ManyToOne
    private MgUser userId;  //유저번호(FK)

    private String fmrecipeCategory; //카테고리 설정

    @Column(length = 100)
    private String fmrecipeTitle;  //fm레시피 제목

    @Column(columnDefinition = "TEXT")
    private String fmrecipeContent;//fm레시피 내용

    private String fmrecipeRegDate;  //작성일

    @Column(columnDefinition = "Integer default 0")
    @NonNull
    private String fmrecipeCnt;   //추천수

    private String fmrecipeFilePath;  //이미지,파일

    private String fmrecipeFileName;  //이미지 파일 이름



}
