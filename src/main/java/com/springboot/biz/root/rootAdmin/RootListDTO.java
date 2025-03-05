package com.springboot.biz.root.rootAdmin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootListDTO {

    private String title;  //목적지명

    private String address;  //주소

    private String roadaddress; //도로명

    private Double latitude;  //위도

    private Double longitude;  //경도
}
