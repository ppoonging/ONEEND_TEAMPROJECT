package com.springboot.biz.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HUserRepository extends JpaRepository<HUser,Integer> {


    Optional<HUser> findByUsername(String username);

}
