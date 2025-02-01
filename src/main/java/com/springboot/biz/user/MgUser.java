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

    private String password;

    private String username;//이름

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    private String address;

    private String addressDetail;

    private double latitude; //위도

    private double longitude; //경도


    private LocalDateTime createDate;


    //위도
    //경도
    //프로필 사진??????

}
