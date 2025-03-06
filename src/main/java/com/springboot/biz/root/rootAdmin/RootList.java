package com.springboot.biz.root.rootAdmin;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RootList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer RootListSeq;

    @ManyToOne
    @JoinColumn(name = "root_seq")
    @JsonBackReference
    private Root root; // 루트

    @ManyToOne
    private HUser userId;  //유저 아이디(FK)

    private String rootListTitle;  //목적지명

    private String rootListAddress;  //주소

    private String rootListRodeAddress; //도로명

    private Double rootListLatitude;  //위도

    private Double rootListLongitude;  //경도

    private String rootListLink; // 장소 정보 url

    private String rootListCategory; // 장소 분류


}
