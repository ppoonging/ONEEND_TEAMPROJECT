package com.springboot.biz.random;

import com.springboot.biz.recipe.fm.FmRecipe;
import com.springboot.biz.recipe.fm.FmRecipeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomService {

    private final FmRecipeRepository fmRecipeRepository;
    private List<FmRecipe> cachedRecipes = new ArrayList<>();
    private LocalDate lastUpdatedDate;

    @PostConstruct
    public void init() {
        refreshRandomRecipes(); // 서버 시작 시 레시피 캐싱
    }

    @Scheduled(cron = "0 0 12 * * ?") // 매일 12시 리셋
    public void refreshRandomRecipes() {
        long count = fmRecipeRepository.count();
        if (count < 5) {
            cachedRecipes = fmRecipeRepository.findAll(); // 5개 이하일 경우 전체 반환
        } else {
            cachedRecipes = getRandomRecipes(5);
        }
        lastUpdatedDate = LocalDate.now();
    }

    private List<FmRecipe> getRandomRecipes(int num) {
        long count = fmRecipeRepository.count();
        List<FmRecipe> randomRecipes = new ArrayList<>();
        Random random = new Random();
        List<Integer> selectedIndices = new ArrayList<>();

        while (randomRecipes.size() < num) {
            int randomIndex = random.nextInt((int) count);
            if (!selectedIndices.contains(randomIndex)) {
                selectedIndices.add(randomIndex);
                fmRecipeRepository.findAll(PageRequest.of(randomIndex, 1))
                        .stream().findFirst()
                        .ifPresent(randomRecipes::add);
            }
        }
        return randomRecipes;
    }

    public List<FmRecipe> getRandomRecipeList() {
        if (lastUpdatedDate == null || !lastUpdatedDate.equals(LocalDate.now())) {
            refreshRandomRecipes(); // 새로운 날이면 레시피 갱신
        }
        return cachedRecipes; // 페이지 새로고침 시 기존 리스트 유지
    }
}
