package com.springboot.biz.free.answer;


import com.springboot.biz.free.board.FreeQuestion;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class FreeAnswer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer frboAnSeq; //글번호

    @ManyToOne
    private HUser userId; // 유저 번호 (FK)

    private Integer frboSeq; //질문 번호 (FK)

    @Column(columnDefinition = "TEXT")
    private String frboAnsContent; //자유게시판 답변 내용

    private LocalDateTime frboARegDate; //답변 날짜

    private LocalDateTime frboAnsModify;  //수정시간


    @ManyToOne
    private FreeQuestion freeQuestion; //관계맺기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_frboAnSeq")
    private FreeAnswer parentAnswer; // 부모 댓글 (대댓글 구현)

    @OneToMany(mappedBy = "parentAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeAnswer> childAnswers = new ArrayList<>();

    @ManyToOne
    private HUser frboAnsAuthor;


}
