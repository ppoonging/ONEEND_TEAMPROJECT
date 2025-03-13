package com.springboot.biz.root.rootAdmin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RootListRepository extends JpaRepository<RootList, Integer> {
    @Query("SELECT r FROM RootList r WHERE r.root.RootSeq = :rootSeq")
    List<RootList> findByRootId(@Param("rootSeq") Integer rootSeq);

    void deleteByRoot(Root root);
}
