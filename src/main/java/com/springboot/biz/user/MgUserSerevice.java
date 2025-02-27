package com.springboot.biz.user;


import com.springboot.biz.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class MgUserSerevice {

    private final MgUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public MgUser create(String username, String password, String nickname, String email,
                         String phoneNumber,String birthday, String address, String addressDetail,String zipCode){
        MgUser mgUser= new MgUser();
        mgUser.setUsername(username);
        mgUser.setPassword(passwordEncoder.encode(password));
        mgUser.setNickname(nickname);
        mgUser.setEmail(email);
        mgUser.setPhoneNumber(phoneNumber);
        mgUser.setBirthday(birthday);
        mgUser.setAddress(address);
        mgUser.setAddressDetail(addressDetail);
        mgUser.setZipCode(zipCode);
        mgUser.setCreateDate(LocalDateTime.now());
        mgUser.setRole(MgUserRole.USER);
        this.userRepository.save(mgUser);
        return mgUser;
    }

    public MgUser getUser(String username){
        Optional<MgUser> mgUser = this.userRepository.findByUsername(username);
        if(mgUser.isPresent()){
            return mgUser.get();
        }else{
            throw new DataNotFoundException("데이터 없음");
        }

    }


}
