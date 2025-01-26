package com.springboot.biz.user;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mg")
public class MgUserController {


    private final MgUserSerevice mgUserSerevice;


    private String ClientId ="Gl8whwhcFMnzNtBefoyT";
    private String clientSecret="9cJxpUH8aU";

  /*  @GetMapping("/naverlogin")
    public String GetLogin(){
        return "naverLogin";
    }

    @PostMapping("/naverlogin")
    public void pstLogin(HttpServletResponse response) throws IOException {
        String basicUri="https://nid.naver.com/oauth2.0/authorize";
        String uri= UriComponentsBuilder
                .fromUriString(basicUri)
                .queryParam("response_type","code")
                .queryParam("client_id",ClientId)// 왜 여기는 ""가 안붙지.....
                .queryParam("redirect_uri","http://localhost:8080/naver-login")
                .queryParam("state" ,"1111")
                .build().toString();
        response.sendRedirect(uri);
    }
    @GetMapping("/naver-login")
    public String getNaverLogin(@RequestParam("code") String code, @RequestParam("state") String state, Model  model, HttpSession session)  {

        NaverUser user = new NaverUser(code,state);

        session.setAttribute("loginUser",user);
        model.addAttribute("loggedIn",user);
        return "naverHome";

    }
*/

    @GetMapping("/signup")
    public String userCreate(MgUser mguser) {
        return "signup_form";
    }


/*
    @PostMapping("/signup")
    public String userCreate(@Valid MgteUserForm diaryUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!diaryUserForm.getPassword1().equals(diaryUserForm.getPassword2())) {
            bindingResult.rejectValue("passwword2", "passwordError", "두 개의 비밀번호가 다릅니다.");
            return "signup_form";
        }
        try {
            siteUserSerevice.create(diaryUserForm.getMyName(), diaryUserForm.getUsername()
                    , diaryUserForm.getPassword1(), diaryUserForm.getEmail(), diaryUserForm.getPhoneNumber());

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("fall", "이미 등록된 사용자입니다");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("fall", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }
*/


    @GetMapping("/login")
    public String login() {
        return "login_form";
    }



    @GetMapping("/mypage")
    public String infor(Model model, Principal principal) {
        MgUser mgUser = this.mgUserSerevice.getUser(principal.getName());
        model.addAttribute("siteUser", mgUser);
        return "mypage_Form";
    }
}