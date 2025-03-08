package com.springboot.biz.root.rootUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootAuthListDTO {

    private String title;  //목적지명

    private String address;  //주소

    private String roadaddress; //도로명

    private Double latitude;  //위도

    private Double longitude;  //경도

    private String link; // 장소 정보 url

    private String category; // 장소 분류

    private String imgName;

    private String imgPath;

}
