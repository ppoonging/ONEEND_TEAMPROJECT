package com.springboot.biz.root.rootUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RootAuthRepository extends JpaRepository<RootAuth, Long> {

    Page<RootAuth> findAll(Pageable pageable);

    // 메서드명 바꾸면 안됨
    Page<RootAuth> findByRoot_RootTitle(String rootTitle, Pageable pageable);
}
