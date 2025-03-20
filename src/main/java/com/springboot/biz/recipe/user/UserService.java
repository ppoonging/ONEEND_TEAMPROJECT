package com.springboot.biz.recipe.user;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRecipeRepository userRecipeRepository;

    private final HUserRepository hUserRepository;

//수정
     //레시피 리스트

    public Page<UserRecipe> getList(String kw, String category, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("userRecipeRegDate"));
        PageRequest pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ((kw == null || kw.isEmpty()) && (category == null || category.isEmpty())) {
            return userRecipeRepository.findAll(pageable);
        }

        if (category == null || category.isEmpty()) {
            return userRecipeRepository.findAllByKeyword(kw, pageable);
        }

        return userRecipeRepository.findByKeywordAndCategory(kw, category, pageable);
    }


     //레시피 저장
    public void createRecipe(UserRecipe userRecipe, MultipartFile file, HUser user) throws IOException {
        userRecipe.setUserId(user);
        userRecipe.setUserRecipeRegDate(LocalDateTime.now());
        userRecipe.setUserRecipeCnt("0");
        userRecipe.setRecipeRecommend(0);

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/userRecipe";

        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename(); // 원본 파일명
            String savePath = projectPath + "/" + fileName; // 저장 경로

            file.transferTo(new File(savePath)); // 실제 저장

            // DB에 저장될 경로 (웹에서 접근 가능)
            userRecipe.setUserRecipeFilePath("/files/userRecipe/" + fileName); // static 하위는 바로 접근
            userRecipe.setUserRecipeFileName(fileName); // 파일명 저장
        }

        // 저장
        userRecipeRepository.save(userRecipe);
    }



  //상세 조회 (조회수 증가)

    public UserRecipe getRecipe(Integer id) {
        UserRecipe recipe = userRecipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
        int cnt = Integer.parseInt(recipe.getUserRecipeCnt());
        recipe.setUserRecipeCnt(String.valueOf(cnt + 1));
        userRecipeRepository.save(recipe);
        return recipe;
    }


  // 추천 (좋아요)

    public void recommendRecipe(Integer id) {
        UserRecipe recipe = userRecipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
        recipe.setRecipeRecommend(recipe.getRecipeRecommend() + 1);
        userRecipeRepository.save(recipe);
    }

    // HUserService.java
    public List<UserRecipe> getRecommendedRecipes() {
        return userRecipeRepository.findByRecipeRecommendGreaterThan(0);  // 추천 수가 0보다 큰 레시피들만 가져옴
    }

}




