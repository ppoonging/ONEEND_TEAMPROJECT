package com.springboot.biz.recipe.fm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface FmRecipeRepository extends JpaRepository<FmRecipe, Integer> {

    @Query("SELECT f FROM FmRecipe f " +
            "WHERE f.fmrecipeTitle LIKE %:kw% OR f.fmrecipeContent LIKE %:kw%")
    Page<FmRecipe> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("SELECT f FROM FmRecipe f " +
            "WHERE (:kw IS NULL OR f.fmrecipeTitle LIKE %:kw% OR f.fmrecipeContent LIKE %:kw%) " +
            "AND (:category IS NULL OR f.fmrecipeCategory = :category)")
    Page<FmRecipe> findByKeywordAndCategory(@Param("kw") String kw,
                                            @Param("category") String category,
                                            Pageable pageable);
}
