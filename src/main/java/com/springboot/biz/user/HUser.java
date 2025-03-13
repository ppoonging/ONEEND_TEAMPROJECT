package com.springboot.biz.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
public class HUser {


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

    private String role;  //유저권한

    private boolean active = true;



    @Column(name = "reset_token", nullable = true)
    private String resetToken;  //토큰 값


    //추천 기능위해 추가
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HUser hUser = (HUser) o;
        return Objects.equals(userSeq, hUser.userSeq);
    }
    public int hashCode() {
        return Objects.hash(userSeq);
    }

}
