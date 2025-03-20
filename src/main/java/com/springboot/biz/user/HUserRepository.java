package com.springboot.biz.user;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HUserRepository extends JpaRepository<HUser,Integer> {


    Optional<HUser> findByUsername(String username);


    Optional<HUser> findByUsernameAndActive(String username, boolean active);

    Optional<HUser> findByEmail(String email);




    Optional<HUser> findByResetToken(String resetToken);


}
