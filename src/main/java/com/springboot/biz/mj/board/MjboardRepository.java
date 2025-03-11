package com.springboot.biz.mj.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MjboardRepository extends JpaRepository<Mjboard, Integer> {
    // 제목으로 검색
    Page<Mjboard> findByMjTitleContaining(String kw, Pageable pageable);
}
