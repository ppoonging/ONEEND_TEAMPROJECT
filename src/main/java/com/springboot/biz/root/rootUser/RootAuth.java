package com.springboot.biz.root.rootUser;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootList;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class RootAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rootAuthSeq;

    @ManyToOne
    private HUser userId;  //유저 아이디(FK)

    @NotNull
    private String rootAuthTitle;

    @Column(columnDefinition = "TEXT")
    private String rootAuthContent;

    @OneToOne
    private Root root;

    private LocalDateTime rootAuthDate;

    @OneToMany(mappedBy = "rootAuth", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<RootAuthList> rootAuthList;

}
