package com.springboot.biz.recipe.fm;


import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class FmRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fmrecipeSeq;   //fm레시피 기본키

    @Column(length = 100)
    private String fmrecipeTitle;  //fm레시피 제목

    @Column(columnDefinition = "TEXT")
    private String fmrecipeContent;//fm레시피 내용

    private String fmrecipeRegDate;  //작성일

    private String fmrecipeCnt;   //추천수

    private String fmrecipeImage;  //이미지


    private Integer userSeq; // 유저 번호 (FK)
}
