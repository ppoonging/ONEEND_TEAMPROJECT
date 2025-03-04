package com.springboot.biz.root.rootAdmin;

import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Root {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer RootSeq;

    @ManyToOne
    private MgUser userId;  //유저 아이디(FK)

    private String rootTitle;  //루트 제목

    @OneToMany(mappedBy = "root", cascade = CascadeType.REMOVE)
    private List<RootList> rootList = new ArrayList<>(); // 루트 목록

    private LocalDateTime rootDate;

//    public void setRootList(List<RootList> rootList) {
//        this.rootList = rootList;
//        for (RootList rl : rootList) {
//            rl.setRoot(this); // 연관 관계 설정
//        }
//    }


}
