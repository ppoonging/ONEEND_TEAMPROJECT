package com.springboot.biz.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;


@Getter
@Setter
public class HUserForm {
    @Size(min =3, max=20)
    @NotEmpty(message = "아이디를 입력하세요")
    private String username;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

    @NotEmpty(message = "비밀번호 확인를 입력하세요")
    private String password2;

    @Size(min =3, max=20)
    @NotEmpty(message = "닉네임을 입력하세요")
    private String nickname;

    @Email
    @NotEmpty(message = "이메일을를 입력하세요")
    private String email;

    @NotEmpty(message = "전화번호를 입력하세요")
    private String phoneNumber;

    @Size(max = 8)
    @NotEmpty(message = "생년월일 8자리를 입력하세요")
    private String birthday;

    @NotEmpty(message = "주소를 입력하세요")
    private String address;

    @NotEmpty(message = "동,읍,면 까지는 입력하세요")
    private String addressDetail;

    private String zipCode;


}
