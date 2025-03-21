package com.springboot.biz.free.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FreeQuestionRepository extends JpaRepository<FreeQuestion, Integer> {


    //페이징처리
    @Override
    Page<FreeQuestion> findAll(Pageable pageable);

    //제목으로 검색
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


    //추천순으로 검색
    @Query("SELECT q FROM FreeQuestion q LEFT JOIN q.freeCnt cnt " +
            "WHERE (q.frboTitle LIKE %:kw% OR q.frboContent LIKE %:kw%) " +
            "GROUP BY q " +
            "ORDER BY COUNT(cnt) DESC")
    Page<FreeQuestion> findAllByRecommend(@Param("kw") String kw,
                                          @Param("searchType") String searchType,
                                          Pageable pageable);


    //최신 등록일기준으로 5개 검색(메인에 들어감)
    List<FreeQuestion> findTop5ByOrderByFrboRegDateDesc();

}
