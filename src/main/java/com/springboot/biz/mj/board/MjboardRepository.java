package com.springboot.biz.mj.board;

import com.springboot.biz.free.board.FreeQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MjboardRepository extends JpaRepository<Mjboard, Integer> {
    // 제목, 내용, 닉네임에서 검색
    @Query("SELECT m FROM Mjboard m WHERE m.mjTitle LIKE %:kw% OR m.mjContent LIKE %:kw% OR m.userId.nickname LIKE %:kw%")
    Page<Mjboard> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    //조회수 기준 TOP 9 게시글 가져오기
    @Query("SELECT m FROM Mjboard m ORDER BY m.mjCnt DESC LIMIT 9")
    List<Mjboard> findTop9ByOrderByMjCntDesc();

    //제목으로 검색
    @Query("SELECT m FROM Mjboard m WHERE m.mjTitle LIKE %:kw%")
    Page<Mjboard> findAllByTitle(@Param("kw") String kw, Pageable pageable);

    //내용으로 검색
    @Query("SELECT m FROM Mjboard m WHERE m.mjContent LIKE %:kw%")
    Page<Mjboard> findAllByContent(@Param("kw") String kw, Pageable pageable);


}

