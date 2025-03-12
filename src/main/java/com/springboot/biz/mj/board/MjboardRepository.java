package com.springboot.biz.mj.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MjboardRepository extends JpaRepository<Mjboard, Integer> {
    // 제목으로 검색
   /* Page<Mjboard> findByMjTitleContaining(String kw, Pageable pageable);*/

    @Query("SELECT m FROM Mjboard m WHERE m.mjTitle LIKE %:kw% OR m.mjContent LIKE %:kw% OR m.userId.nickname LIKE %:kw%")
    Page<Mjboard> findAllByKeyword(@Param("kw") String kw, Pageable pageable);



}
