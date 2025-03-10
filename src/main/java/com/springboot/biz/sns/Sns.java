package com.springboot.biz.sns;


import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sns{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer snsSeq;

    private String snsImagePath; //SNS 이미지 업로드

    private String snsLink;  //SNS URL 경로

    private String snsImageName;  //파일 이름

    private String snsTag;

    @ManyToOne
    private HUser userId;  //유저 아이디(FK)
}
