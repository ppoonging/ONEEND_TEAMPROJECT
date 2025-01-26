package com.springboot.biz.freequestion;


import com.springboot.biz.freeanswer.FreeAnswer;
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
    private Integer freeQseq; //글번호

    @Column(length = 200)
    private String freeQTitle; //제목

    @Column(columnDefinition = "TEXT")
    private String freeQContent; //내용

    private LocalDateTime freeQRegDate; //작성일

    //질문에 대한 답변
    @OneToMany(mappedBy = "freeQuestion", cascade = CascadeType.REMOVE)
    private List<FreeAnswer> answerList;

    /*@ManyToOne
    private Set<> freeCnt; 추천*/

   /* private int freeImg;*/
}
