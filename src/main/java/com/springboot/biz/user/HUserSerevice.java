package com.springboot.biz.user;



import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.notion.MgNotion;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HUserSerevice {
    private final HUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public HUser create(String username, String password, String nickname, String email,
                        String phoneNumber,String birthday, String address, String addressDetail,String zipCode){
        HUser hUser= new HUser();
        hUser.setUsername(username);
        hUser.setPassword(passwordEncoder.encode(password));
        hUser.setNickname(nickname);
        hUser.setEmail(email);
        hUser.setPhoneNumber(phoneNumber);
        hUser.setBirthday(birthday);
        hUser.setAddress(address);
        hUser.setAddressDetail(addressDetail);
        hUser.setZipCode(zipCode);
        hUser.setCreateDate(LocalDateTime.now());
        this.userRepository.save(hUser);
        return hUser;
    }

    public HUser getUser(String username){
        return userRepository.findByUsernameAndActive(username, true)
                .orElseThrow( () -> new UsernameNotFoundException("활성화된 사용자가 아닙니다"));
    }


    public HUser getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }



    /*추가 사용자 정보조회*/
    public HUser getUserById(Integer userSeq) {
        return userRepository.findById(userSeq)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userSeq));

    }

    public Integer getLoggedInUserSeq() {
        return 1; // seq번호 나중에 바꿔줘야함
    }

    public HUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public void modify(HUser hUser, String nickname, String email, String phoneNumber, String address) {
        hUser.setNickname(nickname);
        hUser.setEmail(email);
        hUser.setPhoneNumber(phoneNumber);
        hUser.setAddress(address);
        this.userRepository.save(hUser);
    }

    public void delete(HUser hUser) {
        hUser.setActive(false);
       this.userRepository.save(hUser);
    }



}
