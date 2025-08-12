package com.springboot.biz.root.rootUser;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootRepository;
import com.springboot.biz.user.HUser;
import jakarta.transaction.Transactional;
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
import java.util.*;

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

    public List<RootAuthList> getRootList(Long rootAuthSeq) {
        return rootAuthListRepository.findByRootAuthId(rootAuthSeq);
    }


    // rootAuth list 가져오기
    public Page<RootAuth> getAll(int page) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Order.asc("rootAuthDate")));

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
    public Page<RootAuth> getAllCategory(int page, String category) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Order.desc("rootAuthDate")));
        return rootAuthRepository.findByRoot_RootTitle(category, pageable);
    }

    public void delete(Long rootAuthSeq) {
        List<RootAuthList> rootAuthList = this.rootAuthListRepository.findByRootAuthId(rootAuthSeq);
        for(RootAuthList rel : rootAuthList) {
            Long seq = rel.getRootAuth().getRootAuthSeq();
           if(Objects.equals(seq, rootAuthSeq)){
               this.rootAuthListRepository.deleteById(seq);
           }
        }
        this.rootAuthRepository.deleteById(rootAuthSeq);
    }

    // 저장
    public void save(List<MultipartFile> files, String title, String content,
                     List<RootAuthListDTO> rootAuthListDTO, HUser user, Root root) throws IOException {
        RootAuth rootAuth = new RootAuth();
        rootAuth.setRootAuthTitle(title);
        rootAuth.setRootAuthContent(content.replace("\n", "<br/>"));
        rootAuth.setUserId(user);
        rootAuth.setRoot(root);
        rootAuth.setRootAuthList(new ArrayList<>());
        rootAuth.setRootAuthDate(LocalDateTime.now());

        this.rootAuthRepository.save(rootAuth);

        String projectPath = "/home/ubuntu/oneend/files/rootauth";

        File directory = new File(projectPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Integer index = 1;

        for (int i = 0; i < rootAuthListDTO.size(); i++) {
            RootAuthListDTO rel = rootAuthListDTO.get(i);

            // null 이 아니고 list보다 파일 수가 작으면 파일 가져오기
            MultipartFile file = (files != null && files.size() > i) ? files.get(i) : null;

            String imgName = null;
            String imgPath = null;

            // 파일 있으면
            if (file != null && !file.isEmpty()) {
                String originalImgName = file.getOriginalFilename();
                UUID uuid = UUID.randomUUID();
                imgName = uuid + "_" + originalImgName;
                imgPath = "/files/rootauth/" + imgName;
                File saveFile = new File(projectPath, imgName);
                file.transferTo(saveFile);
            }

            RootAuthList list = RootAuthList.builder()
                    .rootAuth(rootAuth)
                    .rootAuthListTitle(rel.getRootAuthListTitle())
                    .rootAuthListAddress(rel.getRootAuthListAddress())
                    .rootAuthListRodeAddress(rel.getRootAuthListRoadAddress())
                    .rootAuthListLatitude(rel.getRootAuthListLatitude())
                    .rootAuthListLongitude(rel.getRootAuthListLongitude())
                    .rootAuthListLink(rel.getRootAuthListLink())
                    .rootAuthListCategory(rel.getRootAuthListCategory())
                    .rootAuthListIndex(index)
                    .rootAuthListImageName(imgName)
                    .rootAuthListImagePath(imgPath)
                    .userId(user)
                    .build();

            this.rootAuthListRepository.save(list);
            index++;
        }
    }

    @Transactional
    public void modify(List<MultipartFile> files, String title, String content,
                       List<RootAuthListDTO> rootAuthListDTO, HUser user, Root root, Long rootAuthSeq) throws IOException {

        RootAuth rootAuth = rootAuthRepository.findById(rootAuthSeq)
                .orElseThrow(() -> new IllegalArgumentException("수정할 데이터가 없습니다."));

        rootAuth.setRootAuthTitle(title);
        rootAuth.setRootAuthContent(content.replace("\n", "<br/>"));
        rootAuth.setRoot(root);

        rootAuth.setRootAuthModifyDate(LocalDateTime.now());

        rootAuthListRepository.deleteByRootAuth(rootAuth);


        String projectPath = "/home/ubuntu/oneend/files/rootauth";

        File directory = new File(projectPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Integer index = 1;


        for (int i = 0; i < rootAuthListDTO.size(); i++) {
            RootAuthListDTO rel = rootAuthListDTO.get(i);

            // 파일 처리
            MultipartFile file = (files != null && files.size() > i) ? files.get(i) : null;

            String imgName = rel.getRootAuthListImageName();
            String imgPath = rel.getRootAuthListImagePath();

            // 파일이 존재할 때만 새로 저장
            if (file != null && !file.isEmpty()) {
                String originalImgName = file.getOriginalFilename();
                UUID uuid = UUID.randomUUID();
                imgName = uuid + "_" + originalImgName;
                imgPath = "/files/rootauth/" + imgName;
                File saveFile = new File(projectPath, imgName);
                file.transferTo(saveFile);
            }

            // 루트 리스트 저장
            RootAuthList list = RootAuthList.builder()
                    .rootAuth(rootAuth)
                    .rootAuthListTitle(rel.getRootAuthListTitle())
                    .rootAuthListAddress(rel.getRootAuthListAddress())
                    .rootAuthListRodeAddress(rel.getRootAuthListRoadAddress())
                    .rootAuthListLatitude(rel.getRootAuthListLatitude())
                    .rootAuthListLongitude(rel.getRootAuthListLongitude())
                    .rootAuthListLink(rel.getRootAuthListLink())
                    .rootAuthListCategory(rel.getRootAuthListCategory())
                    .rootAuthListIndex(index)
                    .rootAuthListImageName(imgName)
                    .rootAuthListImagePath(imgPath)
                    .userId(user)
                    .build();

            this.rootAuthListRepository.save(list);
            index++;

            rootAuthRepository.save(rootAuth);
        }

    }


}
