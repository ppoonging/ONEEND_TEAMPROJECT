package com.springboot.biz.tour.mjtour;

import com.springboot.biz.tour.mjroot.Root;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tourSeq;

    @ManyToOne
    private HUser userId;  //유저 아이디(FK)

    private String tourTitle;  //투어 루트 제목

    @OneToMany(mappedBy = "tourSeq" , cascade = CascadeType.REMOVE )
    private List<Root> tourList;  //투어루트 목적지 모음



}
