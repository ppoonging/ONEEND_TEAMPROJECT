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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Mjboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjSeq;

    @ManyToOne
    private HUser userId; // 유저 번호 (FK)

    @Column(length = 100)
    private String mjTitle;   //제목

    @Column(columnDefinition = "LONGTEXT") // 긴 내용을 저장학 위해서 변경함.
    private String mjContent;  //내용

    private LocalDateTime mjRegDate;

   /* private Integer mjRecommend;*/ //맛집소개 추천

    private LocalDateTime mjModifyDate;  //맛집소개 수정일

    @Column(columnDefinition = "integer default 0", nullable = false)
    @NonNull
    private Integer mjCnt;  //맛집 조회수

    private String mjthumbnaiurl; //썸네일

    private String mjFilePath;  //맛집소개 파일업로드

    private String mjFileName;  //맛집소개 파일 이름

    @OneToMany(mappedBy = "mjBoard", cascade = CascadeType.REMOVE)
    private List<MjAnswer> mjAanswerList;

    @ManyToMany
    private Set<HUser> recommendUsers = new HashSet<>();
//맛집소개 추천

    // map 관련
    private String mjMapTitle;  //목적지명

    private String mjMapAddress;  //주소

    private String mjMapRodeAddress; //도로명

    private Double mjMapLatitude;  //위도

    private Double mjMapLongitude;  //경도

    private String mjMapLink; // 장소 정보 url

    private String mjMapCategory; // 장소 분류

    //조회수 기준 정렬
    public Integer getMjCnt() {
        return this.mjCnt != null ? this.mjCnt : 0;
    }

    //추천수 기준 정렬
    public Integer getRecommendCount() {
        return this.recommendUsers != null ? this.recommendUsers.size() : 0;
    }




}
