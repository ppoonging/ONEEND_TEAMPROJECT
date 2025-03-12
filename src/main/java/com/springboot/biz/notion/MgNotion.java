package com.springboot.biz.notion;

import com.springboot.biz.user.HUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class MgNotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notionSeq; // 공지사항 번호 (PK)

    @ManyToOne
    private HUser userId; // 유저 번호 (FK)

    @Column( length = 100)
    private String notionTitle; // 공지사항 제목

    @Column(columnDefinition = "TEXT")
    private String notionContent; // 공지사항 내용

    private LocalDateTime notionRegDate; // 공지사항 작성일

    @Column(columnDefinition = "Integer default 0")
    @NonNull
    private int notionCnt; // 공지사항 조회수

    @ManyToOne
    private HUser author;    //작성자

    private LocalDateTime modifyDate;

    private String frboFilePath; //파일경로

    private String frboFileName; //파일이름


}


