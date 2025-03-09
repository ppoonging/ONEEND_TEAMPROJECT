package com.springboot.biz.root.rootUser;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootRepository;
import com.springboot.biz.user.HUser;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RootAuthService {

    private final RootAuthRepository rootAuthRepository;
    private final RootAuthListRepository rootAuthListRepository;
    private final RootRepository rootRepository;

    // root 가져오기
    public List<Root> getRoot() {
        return this.rootRepository.findAll();
    }

    // rootAuth list 가져오기
    public Page<RootAuth> getList(int page) {
        Pageable pageable = PageRequest.of(page, 2, Sort.by(Sort.Order.desc("rootAuthDate")));

        return this.rootAuthRepository.findAll(pageable);
    }

    // RootAuth 가져오기
    public RootAuth get(Long rootAuthSeq) {
        Optional<RootAuth> rootAuth = this.rootAuthRepository.findById(rootAuthSeq);

        if(rootAuth.isPresent()){
            return rootAuth.get();
        }
        throw new DataNotFoundException("데이터가 없습니다");
    }

    // list + category
    public Page<RootAuth> getListCategory(int page, String category) {
        Pageable pageable = PageRequest.of(page, 2, Sort.by(Sort.Order.desc("rootAuthDate")));
        return rootAuthRepository.findByRoot_RootTitle(category, pageable);
    }

    // 저장
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

        if(!multipartFile.isEmpty()) {
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

        }else {
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
                        .rootAuthListImageName(null)
                        .rootAuthListImagePath(null)
                        .userId(user)
                        .build();
                this.rootAuthListRepository.save(list);
                index++;
            }
        }
    }

}
