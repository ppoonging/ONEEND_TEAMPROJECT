package com.springboot.biz.user;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class MgUserController {


    private final MgUserSerevice mgUserSerevice;

    @GetMapping("/signup")
    public String signup(MgUserForm mgUserForm) {
        return "users/signup_form";
    }
    @PostMapping("/signup")
    public String signup( @Valid MgUserForm mguserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/signup_form";
        }
        if(!mguserForm.getPassword().equals(mguserForm.getPassword2())){
            bindingResult.rejectValue("password2","password오류",
                    "비밀번호가 틀립니다.");
            return "users/signup_form";
        }
        try {
            mgUserSerevice.create(mguserForm.getUsername(),  mguserForm.getPassword() ,mguserForm.getNickname(),
                    mguserForm.getEmail(),mguserForm.getPhoneNumber(),
                     mguserForm.getBirthday() ,mguserForm.getAddress(), mguserForm.getAddressDetail(),mguserForm.getZipCode());
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

    @GetMapping("/mypage")
    public String infor(Model model, Principal principal) {
        MgUser mgUser = this.mgUserSerevice.getUser(principal.getName());
        model.addAttribute("siteUser", mgUser);
        return "mypage_Form";
    }
}