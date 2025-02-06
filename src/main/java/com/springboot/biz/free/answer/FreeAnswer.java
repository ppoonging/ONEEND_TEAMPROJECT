package com.springboot.biz.free.answer;


import com.springboot.biz.free.board.FreeQuestion;
import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class FreeAnswer {

    //테스트중
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer frboAnSeq; //글번호

    @ManyToOne
    private MgUser userId; // 유저 번호 (FK)

    private Integer frboSeq; //질문 번호 (FK)

    @Column(columnDefinition = "TEXT")
    private String frboAnsContent; //자유게시판 답변 내용

    private LocalDateTime frboARegDate; //답변 날짜

    private LocalDateTime frboAnsModify;  //수정시간

    private Integer frboAnsRecommend; //답변 추천

    private Integer frboAnsCommend;  //답변에 답변

    @ManyToOne
    private FreeQuestion freeQuestion; //관계맺기



}
