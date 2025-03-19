package com.springboot.biz.notion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MgRepository extends JpaRepository<MgNotion, Integer> {

    Page<MgNotion> findAll(Pageable pageable);
    Page<MgNotion> findByNotionTitleContainingIgnoreCaseOrNotionContentContainingIgnoreCase(String notiontitle, String notioncontent, Pageable pageable);
}
