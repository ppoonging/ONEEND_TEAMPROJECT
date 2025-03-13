package com.springboot.biz.favorite;

import com.springboot.biz.recipe.fm.FmRecipe;
import com.springboot.biz.user.HUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findByUserAndRecipe(HUser user, FmRecipe recipe);
    void deleteByUserAndRecipe(HUser user, FmRecipe recipe);


    List<Favorite> findByUser(HUser user);
}