package com.springboot.biz.root.rootAdmin;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

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
    private HUser userId;  //유저 아이디(FK)

    private String rootTitle;  //루트 제목

    @OneToMany(mappedBy = "root", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<RootList> rootList; // 루트 목록

    private LocalDateTime rootDate;

    private LocalDateTime rootModifyDate;

//    public void setRootList(List<RootList> rootList) {
//        this.rootList = rootList;
//        for (RootList rl : rootList) {
//            rl.setRoot(this); // 연관 관계 설정
//        }
//    }

    private boolean rootState;


}
