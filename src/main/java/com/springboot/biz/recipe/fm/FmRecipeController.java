package com.springboot.biz.recipe.fm;

import com.springboot.biz.favorite.FavoriteService;
import com.springboot.biz.random.RandomService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/fm")
public class FmRecipeController {

    private final FmRecipeService fmRecipeService;
    private final HUserSerevice hUserSerevice;
    private final FavoriteService favoriteService;
    private final FmRecipeRepository fmRecipeRepository;
    private final RandomService randomService;

    @GetMapping("/recipe")
    public String list(Model model,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "category", required = false, defaultValue = "") String category,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Principal principal) {

        // 1. 레시피 리스트 조회 12345
        Page<FmRecipe> paging = fmRecipeService.getList(kw, category, page);
        if (paging == null) paging = Page.empty(); // null 방지 (빈 페이지)

        model.addAttribute("paging", paging); // 리스트
        model.addAttribute("kw", kw); // 검색어
        model.addAttribute("category", category); // 카테고리

        // 2. 찜한 레시피 여부 맵 (비로그인도 빈 맵 넘기기)
        Map<Integer, Boolean> favoriteMap = new HashMap<>();
        if (principal != null) { // 로그인 한 경우에만 체크
            String username = principal.getName();
            for (FmRecipe recipe : paging.getContent()) {
                boolean isFavorite = favoriteService.isFavorite(username, recipe.getFmrecipeSeq());
                favoriteMap.put(recipe.getFmrecipeSeq(), isFavorite);
            }
        }
        model.addAttribute("favoriteMap", favoriteMap); // 항상 넘기기 (비로그인도 empty로)

        return "fm/fmRecipeList"; // 템플릿 경로
    }


    @GetMapping("/create")
        @PreAuthorize("isAuthenticated()")
        public String create() {
            return "fm/fmRecipeCreate";
        }

        @PostMapping("/create")
        @PreAuthorize("isAuthenticated()")  // 로그인한 사용자만 접근 가능 (OAuth2 포함)
        public String questionCreate(Principal principal,FmRecipe fmRecipe
                ,@RequestParam("file") MultipartFile file) throws Exception {

            HUser hUser = this.hUserSerevice.getUser(principal.getName());

            this.fmRecipeService.createRecipe(fmRecipe.getFmrecipeCategory(),
                    fmRecipe.getFmrecipeTitle(),fmRecipe.getFmrecipeIngre(),fmRecipe.getFmrecipeReady()
                    , fmRecipe.getFmrecipeContent(),file);
            return "redirect:/fm/recipe";
        }



}