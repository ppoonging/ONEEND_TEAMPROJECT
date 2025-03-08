package com.springboot.biz.root.rootUser;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RootAuthList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rootAuthListSeq;

    private String rootAuthListImagePath;

    private String rootAuthListImageName;

    private Integer rootAuthListIndex;

    @ManyToOne
    private HUser userId;  //유저 아이디(FK)

    private String rootAuthListTitle;  //목적지명

    private String rootAuthListAddress;  //주소

    private String rootAuthListRodeAddress; //도로명

    private Double rootAuthListLatitude;  //위도

    private Double rootAuthListLongitude;  //경도

    private String rootAuthListLink; // 장소 정보 url

    private String rootAuthListCategory; // 장소 분류

    @ManyToOne
    @JoinColumn(name = "root_auth_seq")
    @JsonBackReference
    private RootAuth rootAuth; // 루트유저인증
}
