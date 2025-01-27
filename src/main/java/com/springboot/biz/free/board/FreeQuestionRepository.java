package com.springboot.biz.free.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeQuestionRepository extends JpaRepository<FreeQuestion, Integer> {


    @Override
    Page<FreeQuestion> findAll(Pageable pageable);
}
