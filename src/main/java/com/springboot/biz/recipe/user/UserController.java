package com.springboot.biz.recipe.user;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/userrecipe") //유저 레시피 관련 URL
public class UserController {

    private final UserService userService;
    private final HUserSerevice hUserSerevice;


   //유저 레시피 리스트 페이지

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "category", required = false, defaultValue = "") String category,
                       @RequestParam(value = "page", defaultValue = "0") int page) {

        model.addAttribute("paging", userService.getList(kw, category, page));
        model.addAttribute("kw", kw);
        model.addAttribute("category", category);

        return "userrecipe/user_list"; // 리스트 페이지 반환
    }


    // 유저 레시피 작성 폼

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
    public String createForm(Model model) {
        model.addAttribute("userRecipe", new UserRecipe()); // 빈 객체 전달
        return "userrecipe/user_form"; // 작성 폼 HTML 연결
    }


     // 유저 레시피 저장 (POST 요청)

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Principal principal,
                         @ModelAttribute UserRecipe userRecipe,
                         @RequestParam("file") MultipartFile file) throws IOException {

        HUser user = hUserSerevice.getUser(principal.getName()); // 현재 로그인한 유저 정보 가져오기
        userService.createRecipe(userRecipe, file, user); // 저장 서비스 호출

        return "redirect:/userrecipe/list"; // 저장 후 리스트로 이동
    }

    //유저 레시피 추천 (좋아요)

    @PostMapping("/recommend/{id}")
    @PreAuthorize("isAuthenticated()")
    public String recommend(@PathVariable Integer id) {
        userService.recommendRecipe(id); // 추천 기능 수행
        return "redirect:/userrecipe/list"; // 추천 후 리스트 페이지 이동
    }

    //유저 레시피 상세 조회 (조회수 증가)

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        UserRecipe recipe = userService.getRecipe(id); // 조회수 증가 포함된 상세 조회
        model.addAttribute("userRecipe", recipe);
        return "userrecipe/user_detail"; // 상세 페이지 HTML 반환
    }

}
