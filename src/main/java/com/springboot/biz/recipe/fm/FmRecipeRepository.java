package com.springboot.biz.recipe.fm;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FmRecipeRepository extends JpaRepository<FmRecipe,Integer> {

    @Query(
            "select distinct f from FmRecipe f "
                    + "where "
                    + "f.fmrecipeContent like %:kw% "
                    + "or f.fmrecipeTitle like %:kw% "

    )
    Page<FmRecipe> findAllBykeyword(Pageable pageable , @Param("kw") String kw);
}
