package com.springboot.biz.free.board;


import com.springboot.biz.free.answer.FreeAnswer;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
public class FreeQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer frboSeq; //글번호

    @ManyToOne
    private HUser userId; // 유저 번호 (FK)

    @Column(length = 200)
    private String frboTitle; //제목

    @Column(columnDefinition = "TEXT")
    private String frboContent; //내용

    private LocalDateTime frboRegDate; //작성일

    @Column(columnDefinition = "Integer default 0")
    @NonNull
    private Integer frboCnt;  //  자유게시판 조회수

    private Integer frboRecommend;  //자유게시판 답변 추천

    private String frboFilePath; //파일경로

    private String frboFileName; //파일이름

    private String frbothumbnailUrl; //썸네일

    private LocalDateTime frboModifyDate; //수정시간

    //질문에 대한 답변
    @OneToMany(mappedBy = "freeQuestion", cascade = CascadeType.REMOVE)
    private List<FreeAnswer> answerList;

    //추가함
    @ManyToOne
    private HUser freeAuthor;//작성자 

    
    @ManyToMany
    private Set<HUser> freeCnt;//추천



}
