package com.springboot.biz.root.rootUser;

import com.springboot.biz.user.HUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RootAuthListDTO {

//    private Long rootAuthListSeq;

//    private Long userId;
//
//    private Integer rootAuthListIndex;

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
