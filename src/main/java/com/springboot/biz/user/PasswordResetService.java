package com.springboot.biz.user;


import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final HUserRepository hUserRepository;


    private final EmailService emailService;

    // 이메일을 확인하고 비밀번호 재설정 토큰을 생성하는 메서드
    public boolean generateResetToken(String email) {
        Optional<HUser> optionalUser = hUserRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return false; // 이메일이 존재하지 않음
        }

        HUser hUser = optionalUser.get();
        String resetToken = UUID.randomUUID().toString(); // 랜덤 토큰 생성
        hUser.setResetToken(resetToken);
        hUserRepository.saveAndFlush(hUser);

        System.out.println("@@@@@저장된 토큰@@@@@" + resetToken);
        try {
            emailService.sendResetPasswordEmail(email, resetToken); // 이메일 발송
        } catch (MessagingException e) {
            return false;
        }

        return true;
    }

    // 비밀번호 재설정 토큰을 검증하는 메서드
    public boolean validateResetToken(String token) {
        Optional<HUser> optionalUser = hUserRepository.findByResetToken(token);
        return optionalUser.isPresent();
    }

    //새로운 비밀번호를 설정하는 메서드. 토큰과 변경할 이메일을 받아온다~~
    public boolean resetPassword(String token, String newpassword) {
        String trimmedToken = token.trim();
        Optional<HUser> optionalUser = hUserRepository.findByResetToken(trimmedToken);

        System.out.println("저장한 값 알려줘제발" +optionalUser.isPresent());

        if (!optionalUser.isPresent()) {
            System.out.println("@@@@@토큰길패");
            return false; // 유효하지 않은 토큰
        }


        HUser hUser = optionalUser.get();
        System.out.println("이메일은 찾았니???" + hUser.getEmail());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        hUser.setPassword(passwordEncoder.encode(newpassword));// 실제 운영에서는 비밀번호 암호화 필수
        hUser.setResetToken(null); // 토큰 제거
        hUserRepository.saveAndFlush(hUser);

        System.out.println("토큰성공가자아아아아아아아아아");
        return true;

    }
}
