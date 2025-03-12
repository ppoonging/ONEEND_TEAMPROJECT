package com.springboot.biz.user;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HUserRepository extends JpaRepository<HUser,Integer> {


    Optional<HUser> findByUsernameAndActive(String username, boolean active);
    Optional<HUser> findByEmail(String email);


    boolean active(boolean active);
    /*Optional<HUser> findByActive(boolean active);*/

    Optional<HUser> findByResetToken(String resetToken);


}
