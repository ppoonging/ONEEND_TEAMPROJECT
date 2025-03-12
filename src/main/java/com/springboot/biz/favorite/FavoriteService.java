package com.springboot.biz.favorite;

import com.springboot.biz.favorite.Favorite;
import com.springboot.biz.favorite.FavoriteRepository;
import com.springboot.biz.recipe.fm.FmRecipe;
import com.springboot.biz.recipe.fm.FmRecipeRepository;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final HUserSerevice hUserSerevice;
    private final FmRecipeRepository fmRecipeRepository;

    // 찜 토글
    public boolean toggleFavorite(String username, Integer recipeId) {
        /*사용자 정보 */
        HUser user = hUserSerevice.getUser(username);
        /*레시피 정보*/
        FmRecipe recipe = fmRecipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피가 존재하지 않습니다."));

        /*찜했는지 찾기*/
        Optional<Favorite> favorite = favoriteRepository.findByUserAndRecipe(user, recipe);
        /*찜했으면 삭제, 없으면 추가*/
        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
            return false;
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setRecipe(recipe);
            favoriteRepository.save(newFavorite);
            return true;
        }
    }

    // 찜 여부 확인
    public boolean isFavorite(String username, Integer recipeId) {
        HUser user = hUserSerevice.getUser(username);
        FmRecipe recipe = fmRecipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피가 존재하지 않습니다."));
        return favoriteRepository.findByUserAndRecipe(user, recipe).isPresent();
    }

    /*마이페이지용*/
    public List<FmRecipe> getUserFavorites(String username) {
        HUser user = hUserSerevice.getUser(username);
        List<Favorite> favorites = favoriteRepository.findByUser(user);

        return favorites.stream()
                .map(Favorite::getRecipe)
                .collect(Collectors.toList());
    }

}
