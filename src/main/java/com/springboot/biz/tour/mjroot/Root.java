package com.springboot.biz.tour.mjroot;


import com.springboot.biz.tour.mjtour.Tour;
import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Root {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rootSeq;  //루트번호(PK)

    @ManyToOne
    private Tour tourSeq;  //투어루트 번호(FK)

    @ManyToOne
    private MgUser userId;  //유저 아이디(FK)

    private String rootDistination;  //목적지명

    private String rootaddress;  //주소

    private Double rootLatitude;  //위도

    private Double rootLongitude;  //경도



}
