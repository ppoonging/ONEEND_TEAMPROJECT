package com.springboot.biz.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class PasswordResetController {
    @Autowired
    private PasswordResetService passwordResetService;

    //비밀번호 찾기 페이지 (이메일 입력)
    @GetMapping("/findPassword")
    public String showForgotPasswordPage() {
        return "users/findEnd";
    }
    // 비밀번호 찾기 요청 처리
    @PostMapping("/findPassword")
    public String forgotPassword(@RequestParam String email, Model model) {
        boolean emailSent = passwordResetService.generateResetToken(email);

        if (!emailSent) {
            model.addAttribute("error", "해당 이메일을 사용하는 회원이 존재하지 않거나 이메일 전송 중 오류가 발생했습니다.");
            return "users/find_form";
        } else {
            model.addAttribute("message", "비밀번호 재설정 이메일을 전송했습니다.");
        }
        return "redirect:/";
    }
    //비밀번호 재설정 페이지
    @GetMapping("/resetPassword")
    public String showResetPasswordPage(@RequestParam(name = "token") String token, Model model) {
        boolean isValidToken = passwordResetService.validateResetToken(token);

        if (!isValidToken) {
            model.addAttribute("error", "유효하지 않은 토큰입니다.");
            return "users/resetPassword_form";
        }

        model.addAttribute("token", token);
        return "users/resetPassword_form";
    }

    // 비밀번호 재설정 요청 처리
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam(name = "token") String token,
                                @RequestParam(name = "newpassword") String newpassword,
                                Model model) {
        System.out.println("토큰 받아와 쮀식아 " + token + "변경하는 비밀번호" + newpassword);
        boolean passwordUpdated = passwordResetService.resetPassword(token, newpassword);

        System.out.println("passwordUpdated: 실패하면 알려ㅝㅈ " + passwordUpdated);
        if (!passwordUpdated) {
            model.addAttribute("error", "유효하지 않은 토큰입니다.");
            return "users/resetPassword_form";
        }
        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        return "users/findEnd";
    }
}
