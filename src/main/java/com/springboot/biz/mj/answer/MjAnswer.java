package com.springboot.biz.mj.answer;


import com.springboot.biz.mj.board.Mjboard;
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
    private Integer mjAnsSeq;  //맛집 기본키

    @ManyToOne
    private MgUser userId; // 유저 번호 (FK)

    private String mjSeq;  //맛집소개 번호(FK)

    @Column(columnDefinition = "TEXT")
    private String mjAnsContent;   //맛집 내용

    private LocalDateTime mjAnsRegDate;  //맛집소개 답변 작성일

    private LocalDateTime mjAnsModifyDate; //맛집소개 답변 수정일

    private Integer mjAnsRecommend;  //맛집소개 답변 추천

    private String mjAnsComment;  //맛집소개 답변에 답변

    @ManyToOne
    private Mjboard mjBoard;   //맛집 질문과 관계맺기

}
