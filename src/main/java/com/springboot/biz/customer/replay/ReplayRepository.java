package com.springboot.biz.customer.replay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplayRepository extends JpaRepository<Replay, Integer> {

    List<Replay> findByCustomer_CustSeq(Integer custSeq);//답변목록 가져옴
}
