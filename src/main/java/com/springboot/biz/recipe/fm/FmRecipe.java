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
    private Integer fmrecipeSeq;

    @Column(length = 100)
    private String fmrecipeTitle;

    @Column(columnDefinition = "TEXT")
    private String fmrecipeContent;

    private String fmrecipeRegDate;

    private String fmrecipeCnt;

    private String fmrecipeImage;


    private Integer userSeq; // 유저 번호 (FK)
}
