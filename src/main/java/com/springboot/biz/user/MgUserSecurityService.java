package com.springboot.biz.user;



import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MgUserSecurityService implements UserDetailsService {

    //아직 네이버를 붙이지 못했어 일단은 그냥 시큐리티.......
    private final MgUserRepository hoUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MgUser> _MgUser = this.hoUserRepository.findByUsername(username);//_안 넣어도 됨

        if (_MgUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }
        MgUser hoUser = _MgUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(MgUserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MgUserRole.USER.getValue()));
        }
        return new User(hoUser.getUsername(), hoUser.getPassword(), authorities);

    }


}


