package com.springboot.biz.notion;

import com.springboot.biz.user.MgUser;
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
    private Integer notioeSeq; // 공지사항 번호 (PK)

    @ManyToOne
    private MgUser userId; // 유저 번호 (FK)

    @Column( length = 100)
    private String notionTitle; // 공지사항 제목

    @Column(columnDefinition = "TEXT")
    private String notionContent; // 공지사항 내용

    private LocalDateTime notionRegDate; // 공지사항 작성일

    @Column(columnDefinition = "Integer default 0")
    @NonNull
    private int noticeCnt; // 공지사항 조회수

    private String notionFilePath; //파일 경로

    private String notionFileName;  //파일 이름

}


