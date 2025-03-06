package com.springboot.biz.recipe.fm;

import com.springboot.biz.sns.Sns;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FmRecipeService {

    private final FmRecipeRepository fmRecipeRepository;
    private final FmReThumbnailService fmReThumbnailService;


    //정형화된 레시피 작성은 admin만 할 수있게 만들아야함
    public void createRecipe(String fmrecipeCategory, String fmrecipeTitle,String fmrecipeIngre
            , String fmrecipeReady,String fmrecipeContent,
                              MultipartFile file) throws IOException {

        /*ㄱ경로*/
        String imgPath = System.getProperty("user.dir")+"/src/main/resources/static/files";

        UUID uuid = UUID.randomUUID();
        /*이미지 이름*/
        String fmrecipeFileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(imgPath,fmrecipeFileName);
        file.transferTo(saveFile);


        FmRecipe fR = new FmRecipe();
        fR.setFmrecipeCategory(fmrecipeCategory);
        fR.setFmrecipeTitle(fmrecipeTitle);
        fR.setFmrecipeIngre(fmrecipeIngre);
        fR.setFmrecipeReady(fmrecipeReady);
        fR.setFmrecipeContent(fmrecipeContent);
        fR.setFmrecipeFilePath("/files/" + fmrecipeFileName );
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
  /* String thumbImgName = "thumb_" + fmrecipeFileName;

        File thumnailFile = new File(imgPath, thumbImgName);


        try {
            fmReThumbnailService.createThumbnail(saveFile, thumnailFile);
        }catch (IOException e) {
            throw new Exception("섬네일 실패", e);
        }
            fmrecipeFilePath = "/files/" + thumbImgName;
*/


