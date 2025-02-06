package com.springboot.biz.recipe.fm;


import org.springframework.data.jpa.repository.JpaRepository;

public interface FmRecipeRepository extends JpaRepository<FmRecipe,Integer> {
}
