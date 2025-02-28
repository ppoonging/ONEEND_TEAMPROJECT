package com.springboot.biz.recipe.fm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FmRecipeService {

    private final FmRecipeRepository fmRecipeRepository;


    //정형화된 레시피 작성은 admin만 할 수있게 만들아야함
    public void createRecipe(String fmrecipeCategory,
                             String fmrecipeTitle,String fmrecipeContent,
                             String fmrecipeFilePath,String fmrecipeFileName) {


        FmRecipe fR = new FmRecipe();
        fR.setFmrecipeCategory(fmrecipeCategory);
        fR.setFmrecipeTitle(fmrecipeTitle);
        fR.setFmrecipeContent(fmrecipeContent);
        fR.setFmrecipeFilePath(fmrecipeFilePath);
        fR.setFmrecipeFileName(fmrecipeFileName);
        fR.setFmrecipeRegDate(LocalDateTime.now());
        fmRecipeRepository.save(fR);
    }
    //페이지 네이션하고, 검색어 (키워드, 카테고리 뭘로하지....?
    public Page<FmRecipe> getList(int page, String kw){
        List<Sort.Order> sorts =new ArrayList<>();
        sorts.add(Sort.Order.desc("fmrecipeRegDate"));//정렬.내림차순(기준이될 놈)
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.fmRecipeRepository.findAllBykeyword(pageable,kw);
    }
}