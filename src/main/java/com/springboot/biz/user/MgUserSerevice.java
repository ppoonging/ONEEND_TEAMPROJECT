package com.springboot.biz.user;



import com.springboot.biz.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MgUserSerevice {
    private final MgUserRepository diaryUserRepository;
    /*private final PasswordEncoder passwordEncoder;*/


    public MgUser create(String userId, String username, String password
            , String email, String phoneNumber, String address, String addressDetail){
        MgUser mgUser= new MgUser();
        mgUser.setUserId(userId);
        mgUser.setUsername(username);
      /*  dU.setPassword(passwordEncoder.encode(password) );*/
        mgUser.setEmail(email);
        mgUser.setPhoneNumber(phoneNumber);
        mgUser.setAddress(address);
        mgUser.setAddressDetail(addressDetail);
        mgUser.setCreateTime(LocalDateTime.now());
        this.diaryUserRepository.save(mgUser);
        return mgUser;
    }

    public MgUser getUser(String username){
        Optional<MgUser> diaryUser = this.diaryUserRepository.findByUsername(username);
        if(diaryUser.isPresent()){
            return diaryUser.get();
        }else{
            throw new DataNotFoundException("데이터 없음");
        }

    }


}
