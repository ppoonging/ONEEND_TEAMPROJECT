package com.springboot.biz.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class MgUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userSeq;

    @Column(unique = true)    //아이디 글자수 제한???
    private String userId;

    private String password;  //비밀번호

    private String username;//이름

    @Column(unique = true)
    private String email; //이메일 정확하게 작성할것

    @Column(unique = true)
    private String phoneNumber;  //유저 폰넘버

    private String address;  //주소

    private String addressDetail;  //상세주소

    private double latitude; //위도

    private double longitude; //경도

    private LocalDateTime createDate;   //만든날짜

}
