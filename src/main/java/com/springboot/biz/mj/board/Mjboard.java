package com.springboot.biz.mj.board;

import com.springboot.biz.free.answer.FreeAnswer;
import com.springboot.biz.mj.answer.MjAnswer;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Mjboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjSeq;

    @ManyToOne
    private HUser userId; // 유저 번호 (FK)

    @OneToMany
    private List<Root> tourSeq;  //투어 루트번호

    @Column(length = 100)
    private String mjTitle;   //제목

    @Column(columnDefinition = "TEXT")
    private String mjContent;  //내용

    private LocalDateTime mjRegDate;

    private Integer mjRecommend; //맛집소개 추천

    private LocalDateTime mjModifyDate;  //맛집소개 수정일

    @Column(columnDefinition = "Integer default 0")
    @NonNull
    private Integer mjCnt;  //맛집 조회수

    private String mjthumbnaiurl; //썸네일

    private String mjFilePath;  //맛집소개 파일업로드

    private String mjFileName;  //맛집소개 파일 이름

    @OneToMany(mappedBy = "mjBoard", cascade = CascadeType.REMOVE)
    private List<MjAnswer> mjAanswerList;


}
