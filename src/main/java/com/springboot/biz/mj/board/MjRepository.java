package com.springboot.biz.mj.board;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MjRepository extends JpaRepository<Mjboard, Integer> {
    Page<Mjboard> findAll(Pageable pageable);

    @Query("select distinct q from Mjboard q "
            +"left outer join HUser u1 on q.userId =u1 "
            +"left outer join MjAnswer a  on a.mjBoard =q "
            +"left outer join HUser u2 on a.userId =u2 "
            +"Where "
            +"q.mjTitle like %:kw% "
            +"or q.mjContent like %:kw% "//작성자
            +"or u1.username like %:kw% "
            +"or a.mjAnsContent like %:kw% " //답변
            +"or u2.username like %:kw% ")
    Page<Mjboard> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
