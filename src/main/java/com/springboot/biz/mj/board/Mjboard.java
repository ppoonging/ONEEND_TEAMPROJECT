package com.springboot.biz.mj.board;

import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Mjboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjSeq;

    private Integer userseq; // 유저 번호 (FK)

    @Column(length = 100)
    private String mjTitle;

    @Column(columnDefinition = "TEXT")
    private String mjContent;

    private LocalDateTime mjRegDate;

    @ManyToOne
    private MgUser author;
/*
    private int mjScore;  //평점
*/

    private double latitude; //위도

    private double longitude; //경도

    private String mjImg;
}
