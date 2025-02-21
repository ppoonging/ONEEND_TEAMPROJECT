package com.springboot.biz.free.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FreeQuestionRepository extends JpaRepository<FreeQuestion, Integer> {




    @Override
    Page<FreeQuestion> findAll(Pageable pageable);


    @Query("select q from FreeQuestion q where q.frboTitle like %:kw%")
    Page<FreeQuestion> findAllByTitle(@Param("kw") String kw, Pageable pageable);

    // 내용으로 검색
    @Query("select q from FreeQuestion q where q.frboContent like %:kw%")
    Page<FreeQuestion> findAllByContent(@Param("kw") String kw, Pageable pageable);

    // 제목과 내용 모두 검색
    @Query("select q from FreeQuestion q where q.frboTitle like %:titleKw% or q.frboContent like %:contentKw%")
    Page<FreeQuestion> findAllByTitleAndContent(@Param("titleKw") String titleKw,
                                                @Param("contentKw") String contentKw,
                                                Pageable pageable);

}
