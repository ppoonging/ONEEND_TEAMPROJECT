package com.springboot.biz.free.answer;


import com.springboot.biz.free.board.FreeQuestion;
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
    private Integer frAnSeq; //글번호

    private Integer userSeq; // 유저 번호 (FK)

    @Column(columnDefinition = "TEXT")
    private String frboAContent; //자유게시판 답변 내용

    private LocalDateTime frboARegDate; //답변 날짜

    @ManyToOne
    private FreeQuestion freeQuestion; //관계맺기



}
