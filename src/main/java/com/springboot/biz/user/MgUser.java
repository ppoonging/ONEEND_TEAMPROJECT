package com.springboot.biz.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class MgUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userSeq;

    @Column(unique = true)    //아이디 글자수 제한???
    private String username;


    private String password;  //비밀번호

    private String nickname;//이름

    @Column(unique = true)
    private String email; //이메일 정확하게 작성할것

    @Column(unique = true)
    private String phoneNumber;  //유저 폰넘버

    private String birthday;  //유저 생년월일 (타입수정 필요)

    private String address;  //주소

    private String addressDetail;  //상세주소

    private String zipCode; //우편번호

    private double latitude; //위도

    private double longitude; //경도

    private LocalDateTime createDate;   //만든날짜

    @Enumerated(EnumType.STRING)
    private MgUserRole role;  //유저권한


}
