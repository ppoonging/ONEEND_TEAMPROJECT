package com.springboot.biz.recipe.user;

import com.springboot.biz.free.board.FreeQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRecipeRepository extends JpaRepository<UserRecipe, Integer> {
//수정을
    // 검색 (제목 + 내용)
    @Query("SELECT u FROM UserRecipe u " +
            "WHERE u.userRecipeTitle LIKE %:kw% OR u.userRecipeContent LIKE %:kw%")
    Page<UserRecipe> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    // 검색 + 카테고리
    @Query("SELECT u FROM UserRecipe u " +
            "WHERE (:kw IS NULL OR u.userRecipeTitle LIKE %:kw% OR u.userRecipeContent LIKE %:kw%) " +
            "AND (:category IS NULL OR u.recipeCategory = :category)")
    Page<UserRecipe> findByKeywordAndCategory(@Param("kw") String kw,
                                              @Param("category") String category,
                                              Pageable pageable);



        // 추천 수가 0보다 큰 레시피들 조회
        List<UserRecipe> findByRecipeRecommendGreaterThan(Integer recommendCount);


    UserRecipe findByUserRecipeSeq(Integer userRecipeSeq);
}