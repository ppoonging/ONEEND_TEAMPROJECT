package com.springboot.biz.favorite;

import com.springboot.biz.recipe.fm.FmRecipe;
import lombok.RequiredArgsConstructor;
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

    // 즐겨찾기 토글

    @PostMapping("/toggle")
    public String toggleFavorite(@RequestParam Integer recipeId, Principal principal) {
        favoriteService.toggleFavorite(principal.getName(), recipeId);
        return "redirect:/fm/recipe"; // 찜 추가/삭제 후 리스트로 리다이렉트
    }

/*마이페이지 즐겨찾기 */
        @GetMapping("/my")
        public String myFavorites(Model model, Principal principal) {
            List<FmRecipe> favoriteRecipes = favoriteService.getUserFavorites(principal.getName());
            model.addAttribute("favoriteRecipes", favoriteRecipes);
            return "users/myfavorite_list"; // 마이페이지 즐겨찾기 목록 페이지
        }
    }


