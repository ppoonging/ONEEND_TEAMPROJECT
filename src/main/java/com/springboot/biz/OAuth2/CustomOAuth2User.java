package com.springboot.biz.OAuth2;

import com.springboot.biz.user.HUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final HUser user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(HUser user, Map<String, Object> attributes) {  // ✅ 생성자 추가
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("cksgh0726@naver.com".equals(user.getEmail())) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return user.getNickname(); // DB에서 가져온 닉네임 사용
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getProfileImage() {
        return user.getProfileImage();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive(); // DB에 저장된 활성화 여부 체크
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
