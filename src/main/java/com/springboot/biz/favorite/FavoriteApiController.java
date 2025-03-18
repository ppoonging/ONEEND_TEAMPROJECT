package com.springboot.biz.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteApiController {  // 새로 만든 API 전용 컨트롤러

    private final FavoriteService favoriteService;

    // AJAX로 즐겨찾기 토글
    @PostMapping("/toggle")
    public Map<String, Object> toggleFavorite(@RequestParam Integer recipeId, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("error", "로그인이 필요합니다.");
            return response;
        }
        boolean isFavorite = favoriteService.toggleFavorite(principal.getName(), recipeId);
        response.put("isFavorite", isFavorite);
        return response;
    }



}

