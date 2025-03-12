package com.springboot.biz.user;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") // 네이버 이메일
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String toEmail, String resetToken) throws MessagingException {
        String subject = " 한식 끝판왕  비밀번호 재설정 안내";
        String resetLink = "http://localhost:8080/resetPassword?token=" + resetToken;
        String content = "<p>******************** 안녕하세요, 한식 끝판왕입니다.********************</p>"
                + "<p>우리는 한식을 조금더 쉽게 알리는 것이 목표입니다.</p>"
                + "<p>아래 버튼을 클릭하여 비밀번호를 재설정하세요:</p>"
                + "<p><a href=\"" + resetLink + "\">비밀번호 재설정</a></p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
