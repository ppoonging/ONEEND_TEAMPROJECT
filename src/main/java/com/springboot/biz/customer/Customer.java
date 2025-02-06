package com.springboot.biz.customer;

import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer custSeq;

    @ManyToOne
    private MgUser userId; // 유저 번호 (FK)

    private String custName; //작성자 이름

    @Column(length = 100)
    private String custTitle;  //고객센터 제목

    private String custContent;   //고객센터 내용

    private LocalDateTime custRegTime;   //작성일

    private boolean custState;  //처리상태

    private String custFilePath;  //파일경로

    private String custFileName;  //파일 이름

}
