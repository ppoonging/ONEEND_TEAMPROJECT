package com.springboot.biz.notion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class MgNotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noticeSeq")
    private Integer notioeSeq; // 공지사항 번호 (PK)

    @Column(name = "userseq")
    private Integer userseq; // 유저 번호 (FK)

    @Column(name = "notionTitle", length = 200)
    private String notionTitle; // 공지사항 제목

    @Column(name = "notionContent")
    private String notionContent; // 공지사항 내용

    @Column(name = "notionRegDate")

    private LocalDateTime notionRegDate; // 공지사항 작성일

    @Column(name = "noticeCnt")
    private int noticeCnt; // 공지사항 조회수
}


