package com.springboot.biz.customer;

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

    /*   private Integer userSeq; //유저 seq*/


    private String custName; //작성자 이름

    @Column(length = 100)
    private String custTitle;

    private String custContent;

    private LocalDateTime custRegTime;

    private boolean custState;  //처리상태

    private String custManyQ; //자주 질문하는 것들
}
