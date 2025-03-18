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

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // naver
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttribute(userNameAttributeName);

        String email = (String) attributes.getOrDefault("email", null);
        String name = (String) attributes.getOrDefault("name", null);
        String profileImage = (String) attributes.getOrDefault("profile_image", null);

        if (email == null) {
            throw new IllegalArgumentException("네이버 응답에 이메일이 없습니다.");
        }

        Optional<HUser> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            HUser newUser = new HUser();
            newUser.setUsername(email);
            newUser.setEmail(email);
            newUser.setNickname(name != null ? name : "네이버사용자");
            newUser.setProfileImage(profileImage);
            newUser.setPassword(passwordEncoder.encode("소셜로그인임시비번"));
            newUser.setActive(true);
            newUser.setCreateDate(LocalDateTime.now());

            userRepository.save(newUser);
        }

        return new CustomOAuth2User(attributes);
    }
}
