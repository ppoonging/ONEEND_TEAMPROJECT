package com.springboot.biz.OAuth2;


import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final HUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional // ⭐️ 꼭 추가 (트랜잭션 보장)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // naver
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // response

        Map<String, Object> attributes = oAuth2User.getAttribute(userNameAttributeName);

        // 데이터 확인
        System.out.println("✅ 네이버 유저 정보: " + attributes);

        // 안전하게 null 체크
        String email = (String) attributes.getOrDefault("email", null);
        String name = (String) attributes.getOrDefault("name", null);
        String profileImage = (String) attributes.getOrDefault("profile_image", null);

        if (email == null) {
            throw new IllegalArgumentException("네이버 응답에 이메일이 없습니다.");
        }

        // DB 확인
        Optional<HUser> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            // 신규 유저 저장
            HUser newUser = new HUser();
            newUser.setUsername(email); // 아이디로 이메일 사용
            newUser.setEmail(email);
            newUser.setNickname(name != null ? name : "네이버사용자"); // 닉네임 없으면 기본값
            newUser.setProfileImage(profileImage); // 있으면 저장
            newUser.setPassword(passwordEncoder.encode("소셜로그인임시비번")); // 임시 비밀번호
            newUser.setActive(true);
            newUser.setCreateDate(LocalDateTime.now());

            userRepository.save(newUser);
            System.out.println("✅ 신규 회원 저장 완료: " + email);
        } else {
            System.out.println("✅ 기존 회원 존재: " + email);
        }

        return new CustomOAuth2User(attributes);
    }
}
