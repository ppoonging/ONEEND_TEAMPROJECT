package com.springboot.biz.SNSBoard;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SNS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer snsSeq;

    private String snsImagePath; //SNS 이미지 업로드

    private String snsLink;  //SNS URL 경로

    private String snsImageName;  //파일 이름
}
