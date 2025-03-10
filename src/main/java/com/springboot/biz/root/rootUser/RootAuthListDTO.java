package com.springboot.biz.root.rootUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootAuthListDTO {

    private String rootAuthListTitle;  //목적지명

    private String rootAuthListAddress;  //주소

    private String rootAuthListRoadAddress; //도로명

    private Double rootAuthListLatitude;  //위도

    private Double rootAuthListLongitude;  //경도

    private String rootAuthListLink; // 장소 정보 url

    private String rootAuthListCategory; // 장소 분류

    private String rootAuthListImageName;

    private String rootAuthListImagePath;

}
