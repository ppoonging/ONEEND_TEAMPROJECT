package com.springboot.biz.mj.board;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Setter
public class MjAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjAnsSeq;

    @Column(name = "userseq")
    private Integer userseq; // 유저 번호 (FK)

    private String mjAnsContent;

    private LocalDateTime createRegDate;

    private String MjAnsImg;


}
