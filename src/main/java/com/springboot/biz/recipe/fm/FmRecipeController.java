package com.springboot.biz.recipe.fm;

import com.springboot.biz.favorite.FavoriteService;
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


@Controller
@RequiredArgsConstructor
@RequestMapping("/fm")
public class FmRecipeController {

    private final FmRecipeService fmRecipeService;
    private final HUserSerevice hUserSerevice;
    private final FavoriteService favoriteService;
    private final FmRecipeRepository fmRecipeRepository;


    @GetMapping("/recipe")
    public String list(Model model, @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "category", required = false, defaultValue = "") String category,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Principal principal) {

        Page<FmRecipe> paging = this.fmRecipeService.getList(kw, category, page);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("category", category);

        // 로그인 되어 있을 때만 즐겨찾기 정보 추가
        // 즐겨찾기 정보 함께 전달
        Map<Integer, Boolean> favoriteMap = new HashMap<>();
        if (principal != null) {
            String username = principal.getName();
            for (FmRecipe recipe : paging.getContent()) {
                boolean isFavorite = favoriteService.isFavorite(username, recipe.getFmrecipeSeq());
                favoriteMap.put(recipe.getFmrecipeSeq(), isFavorite);
            }
        }
        model.addAttribute("favoriteMap", favoriteMap); // 찜 정보 전달

        return "fm/fmRecipeList";
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