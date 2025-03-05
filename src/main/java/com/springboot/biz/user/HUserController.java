package com.springboot.biz.user;


import com.springboot.biz.notion.MgNotion;
import com.springboot.biz.notion.MgNotionForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class HUserController {

    private final HUserSerevice hUserSerevice;

    @GetMapping("/signup")
    public String signup(HUserForm hUserForm) {
        return "users/signup_form";
    }

        @PostMapping("/signup")
            public String signup( @Valid HUserForm hUserForm, BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {

                    return "users/signup_form";

                }
        if(!hUserForm.getPassword().equals(hUserForm.getPassword2())){
            bindingResult.rejectValue("password2","password오류",
                    "비밀번호가 틀립니다.");
            return "users/signup_form";
        }
        try {
            hUserSerevice.create(hUserForm.getUsername(),  hUserForm.getPassword() ,hUserForm.getNickname(),
                    hUserForm.getEmail(),hUserForm.getPhoneNumber(),
                    hUserForm.getBirthday() ,hUserForm.getAddress(), hUserForm.getAddressDetail(),hUserForm.getZipCode());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();

        bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
        return "users/signup_form";

    } catch (Exception e) {
        e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "users/signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "users/login_form";
    }

     /*아이디 찾기*/
     @GetMapping("/findLoginId")
     public String findByEmail(@RequestParam(name = "email", required = false) String email, Model model) {
         if (email == null || email.isEmpty()) {
             model.addAttribute("error", "이메일을 입력해주세요.");
             return "users/find_form"; // 이메일이 없으면 폼으로 다시 돌아감
         }

         HUser hUser = hUserSerevice.getUserByEmail(email);

         if (hUser != null) {
             model.addAttribute("user", hUser);
         } else {
             model.addAttribute("error", "이메일에 해당하는 사용자가 없습니다.");
         }

         return "users/find_form"; // 결과를 보여줄 뷰로 리턴
     }



    @GetMapping("/myPage")
        public String infor(Model model, Principal principal) {
            HUser hUser = this.hUserSerevice.getUser(principal.getName());
            model.addAttribute("hUser", hUser);
            return "myPage";
        }


        @GetMapping("/modify/{username}")
    public String Modify(HUserForm hUserForm, @PathVariable("username") String username) {
         HUser hUser = this.hUserSerevice.getUser(username);

         hUserForm.setNickname(hUser.getNickname());
         hUserForm.setEmail(hUser.getEmail());
         hUserForm.setPhoneNumber(hUser.getPhoneNumber());
         hUserForm.setAddress(hUser.getAddress());
         return "myPageForm";
        }


    @PostMapping("/modify/{username}")
    public String Modify(@PathVariable("username") String username, @Valid HUserForm hUserForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "myPageForm";
        }
        HUser hUser = this.hUserSerevice.getUser(username);

        this.hUserSerevice.modify(hUser, hUserForm.getNickname(), hUserForm.getEmail(), hUserForm.getPhoneNumber(), hUserForm.getAddress());
        return "redirect:/users/myPage";
    }

    }