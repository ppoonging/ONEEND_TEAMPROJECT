package com.springboot.biz.root.rootAdmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RootRepository extends JpaRepository<Root, Integer>  {

    List<Root> findByRootStateTrueOrderByRootDateAsc();
}
