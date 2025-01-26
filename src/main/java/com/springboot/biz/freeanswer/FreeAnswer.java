package com.springboot.biz.freeanswer;


import com.springboot.biz.freequestion.FreeQuestion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class FreeAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq; //글번호

    @Column(columnDefinition = "TEXT")
    private String  freeAContent; //자유게시판 답변 내용

    private LocalDateTime freeARegDate; //답변 날짜

    @ManyToOne
    private FreeQuestion freeQuestion; //관계맺기



}
