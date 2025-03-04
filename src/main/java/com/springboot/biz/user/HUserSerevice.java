package com.springboot.biz.user;



import com.springboot.biz.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HUserSerevice {
    private final HUserRepository userRepository;
   /* private final PasswordEncoder passwordEncoder;*/


    public HUser create(String username, String password, String nickname, String email,
                        String phoneNumber,String birthday, String address, String addressDetail,String zipCode){
        HUser hUser= new HUser();
        hUser.setUsername(username);
      /*  hUser.setPassword(passwordEncoder.encode(password));*/
        hUser.setPassword(password);
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
        Optional<HUser> diaryUser = this.userRepository.findByUsername(username);
        if(diaryUser.isPresent()){
            return diaryUser.get();
        }else{
            throw new DataNotFoundException("데이터 없음");
        }

    }


}
