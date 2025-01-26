package com.springboot.biz.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MgUserRepository extends JpaRepository<MgUser,Integer> {


    Optional<MgUser> findByUsername(String username);

}
