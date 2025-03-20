package com.springboot.biz.recipe.user;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/userrecipe") //유저 레시피 관련 URL
public class UserController {

    private final UserService userService;
    private final HUserSerevice hUserSerevice;
    private final UserRecipeRepository userRecipeRepository;


    //유저 레시피 리스트 페이지
    //수정을 위함
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

    @GetMapping("/detail/{userRecipeSeq}")
    public ResponseEntity<Map<String, Object>> getRecipeDetail(@PathVariable Integer userRecipeSeq) {
        UserRecipe recipe = userRecipeRepository.findByUserRecipeSeq(userRecipeSeq); // 레시피 ID로 조회
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 레시피 없으면 404 반환
        }

        // 레시피 상세 데이터를 반환 (JSON 형식)
        Map<String, Object> response = new HashMap<>();
        response.put("title", recipe.getUserRecipeTitle());
        response.put("content", recipe.getUserRecipeContent()); //레시피
        response.put("ingredients",recipe.getUserrecipeIngre());  //재료
        response.put("ready", recipe.getUserrecipeReady()); //준비
        return ResponseEntity.ok(response);
    }





}
