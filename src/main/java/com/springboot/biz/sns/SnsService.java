package com.springboot.biz.sns;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SnsService {

    private final SnsRepository snsRepository;

    public void save (MultipartFile multipartFile, String link) throws IOException {

        String os = System.getProperty("os.name").toLowerCase();
        String originalImgName = multipartFile.getOriginalFilename();
        String imgName = "";

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/sns";

        UUID uuid = UUID.randomUUID();

        imgName = uuid + "_" + originalImgName;

        File saveFile = new File(projectPath, imgName);

        multipartFile.transferTo(saveFile);

        Sns sns = Sns.builder()
                .snsImageName(imgName)
                .snsImagePath("/uploads/sns/" + imgName)
                .snsLink(link)
                .build();

        this.snsRepository.save(sns);

    }

    public List<Sns> getAll (){
        return this.snsRepository.findAll();
    }

}
