package com.springboot.biz.free.board;


import com.springboot.biz.free.answer.FreeAnswer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
public class FreeQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer frboSeq; //글번호

    @Column(name = "userSeq")
    private Integer userSeq; // 유저 번호 (FK)

    @Column(length = 200)
    private String frboTitle; //제목

    @Column(columnDefinition = "TEXT")
    private String frboContent; //내용

    private LocalDateTime frboRegDate; //작성일

    //질문에 대한 답변
    @OneToMany(mappedBy = "freeQuestion", cascade = CascadeType.REMOVE)
    private List<FreeAnswer> answerList;

    /*@ManyToOne
    private Set<> freeCnt; 추천*/

   /* private int freeImg;*/
}
