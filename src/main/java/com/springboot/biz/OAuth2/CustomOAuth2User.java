package com.springboot.biz.OAuth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 이메일에 따라 권한 설정
        String email = (String) attributes.get("email");
        if ("cksgh0726@naver.com".equals(email)) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return (String) attributes.get("name"); // 네이버 사용자 이름
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getProfileImage() {
        return (String) attributes.get("profile_image");
    }
}
