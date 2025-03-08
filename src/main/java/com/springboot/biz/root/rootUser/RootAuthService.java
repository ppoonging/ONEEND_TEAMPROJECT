package com.springboot.biz.root.rootUser;

import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootRepository;
import com.springboot.biz.user.HUser;
import lombok.RequiredArgsConstructor;
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
public class RootAuthService {

    private final RootAuthRepository rootAuthRepository;
    private final RootAuthListRepository rootAuthListRepository;
    private final RootRepository rootRepository;

    public List<Root> get() {
        return this.rootRepository.findAll();
    }

    public void save(MultipartFile multipartFile, String title, String content,
                     List<RootAuthListDTO> rootAuthListDTO, HUser user, Root root) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String originalImgName = multipartFile.getOriginalFilename();
        String imgName = "";

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/rootauth";

        UUID uuid = UUID.randomUUID();

        imgName = uuid + "_" + originalImgName;

        File saveFile = new File(projectPath, imgName);

        multipartFile.transferTo(saveFile);

        RootAuth rootAuth = new RootAuth();
        rootAuth.setRootAuthTitle(title);
        rootAuth.setRootAuthContent(content);
        rootAuth.setUserId(user);

        List<RootAuthList> rootAuthList = new ArrayList<>();

        rootAuth.setRoot(root);
        rootAuth.setRootAuthList(rootAuthList);
        rootAuth.setRootAuthDate(LocalDateTime.now());

        this.rootAuthRepository.save(rootAuth);

        Integer index = 1;

        for(RootAuthListDTO rel : rootAuthListDTO){
            RootAuthList list = RootAuthList.builder()
                    .rootAuth(rootAuth)
                    .rootAuthListTitle(rel.getTitle())
                    .rootAuthListAddress(rel.getAddress())
                    .rootAuthListRodeAddress(rel.getRoadaddress())
                    .rootAuthListLatitude(rel.getLatitude())
                    .rootAuthListLongitude(rel.getLongitude())
                    .rootAuthListLink(rel.getLink())
                    .rootAuthListCategory(rel.getCategory())
                    .rootAuthListIndex(index)
                    .rootAuthListImageName(imgName)
                    .rootAuthListImagePath("/files/rootauth" + imgName)
                    .userId(user)
                    .build();

            this.rootAuthListRepository.save(list);
            index++;
        }
    }

}
