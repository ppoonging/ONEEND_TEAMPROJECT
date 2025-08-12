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
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttribute(userNameAttributeName);

        String email = (String) attributes.getOrDefault("email", null);
        String name = (String) attributes.getOrDefault("name", null);


        if (email == null) {
            throw new IllegalArgumentException("네이버 응답에 이메일이 없습니다.");
        }

        Optional<HUser> existingUser = userRepository.findByEmail(email);
        HUser user;  // ✅ user 변수를 if-else 바깥에서 선언

        if (existingUser.isPresent()) {
            user = existingUser.get();

            // ✅ 기존 사용자 정보 업데이트 (닉네임만 변경 가능)
            boolean isUpdated = false;
            if (!user.getNickname().equals(name)) {
                user.setNickname(name);
                isUpdated = true;
            }

            if (isUpdated) {
                userRepository.save(user); // 변경 사항 반영
            }

        } else {
            // ✅ 새로운 사용자 생성 후 DB에 저장
            user = new HUser();
            user.setUsername(email); // username은 email과 동일하게 설정
            user.setEmail(email);
            user.setNickname(name != null ? name : "네이버사용자");
            user.setPassword(passwordEncoder.encode("소셜로그인임시비번")); // 비밀번호는 임시로 설정
            user.setActive(true);
            user.setCreateDate(LocalDateTime.now());

            userRepository.save(user);
        }

        // ✅ 이제 user 변수를 사용할 수 있음
        return new CustomOAuth2User(user,attributes);
    }
}
