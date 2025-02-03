package com.springboot.biz.mj.answer;


import com.springboot.biz.user.MgUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Setter
public class MjAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjAnSeq;

    private Integer userseq; // 유저 번호 (FK)

    @Column(columnDefinition = "TEXT")
    private String mjAnsContent;

    @ManyToOne
    private MgUser author;

    private LocalDateTime createRegDate;

    private String MjAnsImg;


}
