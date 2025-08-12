package com.springboot.biz.favorite;

import com.springboot.biz.recipe.fm.FmRecipe;
import com.springboot.biz.recipe.user.UserRecipe;
import com.springboot.biz.recipe.user.UserService;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootService;
import com.springboot.biz.root.rootUser.RootAuth;
import com.springboot.biz.root.rootUser.RootAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final RootAuthService rootAuthService;
    private final RootService rootService;
    // 즐겨찾기 토글

    @PostMapping("/toggle")
    public String toggleFavorite(@RequestParam Integer recipeId, Principal principal) {
        favoriteService.toggleFavorite(principal.getName(), recipeId);
        return "redirect:/fm/recipe"; // 찜 추가/삭제 후 리스트로 리다이렉트
    }

        /*마이페이지 즐겨찾기 */
        @PreAuthorize("isAuthenticated()")
        @GetMapping("/my")
        public String myFavorites(Model model, Principal principal,
                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                  @RequestParam(name = "chkCategory", defaultValue = "전체", required = false) String chkCategory) {
            // 로그인한 유저의 즐겨찾기 레시피 가져오기
            List<FmRecipe> favoriteRecipes = favoriteService.getUserFavorites(principal.getName());

            // 추천된 레시피 가져오기
            List<UserRecipe> recommendedRecipes = userService.getRecommendedRecipes();

            // RootAuth 목록 가져오기
            Page<RootAuth> rootAuthPage;
            if (chkCategory == null || chkCategory.equals("전체")) {
                rootAuthPage = this.rootAuthService.getAll(page);
            } else {
                rootAuthPage = this.rootAuthService.getAllCategory(page, chkCategory);
            }

            List<Root> root = this.rootService.getList();

            // 모델에 데이터 추가
            model.addAttribute("favoriteRecipes", favoriteRecipes);  // 즐겨찾기 레시피
            model.addAttribute("recommendedRecipes", recommendedRecipes);  // 추천 레시피
            model.addAttribute("rootAuthPage", rootAuthPage); // RootAuth 목록 추가
            model.addAttribute("root", root); // Root 목록 추가
            model.addAttribute("chkCategory", chkCategory); // 선택한 카테고리 유지

            return "users/myfavorite_list"; // 마이페이지 즐겨찾기 목록 페이지
        }






}





