package com.springboot.biz.mj.answer;


import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Setter
public class MjAnswer {
    //맛집 답변
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjAnSeq;  //맛집 기본키

    private Integer userseq; // 유저 번호 (FK)

    @Column(columnDefinition = "TEXT")
    private String mjAnsContent;   //맛집 내용

    @ManyToOne
    private MgUser author; //작성자

    private LocalDateTime createRegDate;  //작성일

    private String MjAnsImg;  //이미지


}
