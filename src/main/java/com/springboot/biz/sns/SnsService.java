package com.springboot.biz.sns;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.user.HUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SnsService {

    private final SnsRepository snsRepository;

    public void save (MultipartFile multipartFile, String link, String title, String content, String tag, HUser user) throws IOException {

        String originalImgName = multipartFile.getOriginalFilename();
        String imgName = "";

        // String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/sns";
        String projectPath = "/home/ubuntu/oneend/files/sns";

        File directory = new File(projectPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        UUID uuid = UUID.randomUUID();

        imgName = uuid + "_" + originalImgName;

        File saveFile = new File(projectPath, imgName);

        multipartFile.transferTo(saveFile);

        Sns sns = Sns.builder()
                .snsImageName(imgName)
                .snsImagePath("/files/sns/" + imgName)
                .snsCommentTitle(title)
                .snsCommentContent(content)
                .snsLink(link)
                .snsTag(tag)
                .userId(user)
                .build();

        this.snsRepository.save(sns);

    }

    public List<Sns> getAll (){
        return this.snsRepository.findAll();
    }

    public Sns get(Integer snsSeq) {
        Optional<Sns> sns = this.snsRepository.findById(snsSeq);

        if(sns.isPresent()){
            return sns.get();
        }
        throw new DataNotFoundException("데이터가 없습니다.");
    }

    public void modify(Integer snsSeq, MultipartFile file, String path, String link, String title, String content, String tag, HUser user) throws IOException {
        Sns sns = snsRepository.findById(snsSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));


        String imgName = sns.getSnsImageName();
        String imgPath = sns.getSnsImagePath();

        // String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/sns/";
        String projectPath = "/home/ubuntu/oneend/files/sns";

        File directory = new File(projectPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (file != null && !file.isEmpty()) {
            String originalImgName = file.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            imgName = uuid + "_" + originalImgName;
            imgPath = "/files/sns/" + imgName;
            File saveFile = new File(projectPath, imgName);
            file.transferTo(saveFile);
        } else {
            if (path != null && !path.isEmpty()) {
                imgPath = path;
            }
        }

        sns.setSnsCommentTitle(title);
        sns.setSnsCommentContent(content);
        sns.setSnsTag(tag);
        sns.setSnsLink(link);
        sns.setSnsImageName(imgName);
        sns.setSnsImagePath(imgPath);

        this.snsRepository.save(sns);

    }
    public void delete(Integer snsSeq){
        snsRepository.deleteById(snsSeq);
    }

}
