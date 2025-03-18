package com.springboot.biz.customer.replay;

import com.springboot.biz.customer.question.Customer;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Replay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer replySeq; // 답변 ID

    @ManyToOne
    private Customer customer; // 고객 문의 (FK)

    @ManyToOne
    private HUser user; // 답변 작성자 (관리자)


    private String replayContent; // 답변 내용

    private LocalDateTime replayRegTime = LocalDateTime.now(); // 답변 작성일



}
