package com.springboot.biz.root.rootAdmin;


import com.springboot.biz.user.MgUser;
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
    private Root root; // 루트

    @ManyToOne
    private MgUser userId;  //유저 아이디(FK)

    private String rootListTitle;  //목적지명

    private String rootListAddress;  //주소

    private String rootListRodeAddress; //도로명

    private Double rootListLatitude;  //위도

    private Double rootListLongitude;  //경도



}
