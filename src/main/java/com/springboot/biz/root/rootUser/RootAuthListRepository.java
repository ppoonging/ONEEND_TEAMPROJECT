package com.springboot.biz.root.rootUser;

import com.springboot.biz.root.rootAdmin.RootList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RootAuthListRepository extends JpaRepository<RootAuthList, Long> {
    @Query("SELECT r FROM RootAuthList r WHERE r.rootAuth.rootAuthSeq = :rootAuthSeq")
    List<RootAuthList> findByRootAuthId(@Param("rootAuthSeq") Long rootAuthSeq);

    void deleteByRootAuth(RootAuth rootAuth);
}
