package com.springboot.biz.user;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HUserRepository extends JpaRepository<HUser,Integer> {


    Optional<HUser> findByUsername(String username);
    Optional<HUser> findByEmail(String email);

    Optional<HUser> findByResetToken(String resetToken);


}
