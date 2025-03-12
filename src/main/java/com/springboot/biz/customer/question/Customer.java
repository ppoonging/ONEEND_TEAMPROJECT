package com.springboot.biz.customer.question;

import com.springboot.biz.customer.Replay;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer custSeq;

    @ManyToOne
    private HUser user; // 유저 번호 (FK)  userid->user로 수정

    @Column(length = 100)
    private String custTitle;  //고객센터 제목

    private String custContent;   //고객센터 내용

    private LocalDateTime custRegTime;   //작성일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustState custState = CustState.PENDING;  // 처리상태 (기본: 대기중)

    private String custFilePath;  //파일경로

    private String custFileName;  //파일 이름

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Replay> replay = new ArrayList<>();
}
