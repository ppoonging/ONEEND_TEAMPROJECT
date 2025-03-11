package com.springboot.biz.mj.board;


import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import com.springboot.biz.user.HUserSerevice;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
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
public class MjboardService {
    private final MjboardRepository mjboardRepository;
    private final MjthumbnailService mjthumbnailService;
    private final HUserSerevice hUserSerevice;





    // 게시글 목록
 /*   public Page<Mjboard> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "mjRegDate"));
        return this.mjboardRepository.findAll(pageable);
    }*/
    public Page<Mjboard> getList(Pageable pageable, String kw) {
        if (kw == null || kw.trim().isEmpty()) {
            return mjboardRepository.findAll(pageable);
        } else {
            return mjboardRepository.findByMjTitleContaining(kw, pageable);
        }
    }




    // 게시글 작성 (유저 포함)
    public void create(String mjTitle, String mjContent, MultipartFile file, HUser hUser) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";
        UUID uuid = UUID.randomUUID();
        String mjFileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, mjFileName);
        file.transferTo(saveFile);

        // 썸네일 생성
        String thumbnailFileName = "thumb_" + mjFileName;
        File thumbnailFile = new File(projectPath, thumbnailFileName);
        mjthumbnailService.createThumbnail(saveFile, thumbnailFile);

        String thumbnailUrl = "/files/mj/" + thumbnailFileName;
        String filePath = "/files/mj/" + mjFileName;

        // 저장
        Mjboard mj = new Mjboard();
        mj.setMjFilePath(filePath);
        mj.setMjthumbnaiurl(thumbnailUrl);
        mj.setMjFileName(mjFileName);
        mj.setMjTitle(mjTitle);
        mj.setMjContent(mjContent);
        mj.setMjRegDate(LocalDateTime.now());
        mj.setUserId(hUser);
        this.mjboardRepository.save(mj);
    }

    // 썸머노트 이미지 저장
    public String saveSummernoteImage(MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);
        return "/files/mj/" + fileName;
    }

    // 게시글 조회
    public Mjboard getMjboard(Integer mjSeq) {
        return mjboardRepository.findById(mjSeq)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
    }

    // 수정
    public void modify(Mjboard mjboard, String mjTitle, String mjContent) {
        mjboard.setMjTitle(mjTitle);
        mjboard.setMjContent(mjContent);
        mjboardRepository.save(mjboard);
    }

    // 삭제
    public void delete(Mjboard mjboard) {
        mjboardRepository.delete(mjboard);
    }

    // 추천
    // 추천 기능
    @Transactional
    public int mjRecommend(Mjboard mjboard, HUser user) {
        // 추천한 사용자 목록 가져오기
        List<HUser> recommendList = mjboard.getRecommendUsers();
        // 이미 추천했는지 확인 (userSeq 기준 비교)
        boolean alreadyRecommended = recommendList.stream()
                .anyMatch(u -> u.getUserSeq().equals(user.getUserSeq()));  //userSeq 비교
        if (alreadyRecommended) {
            // 이미 추천했으면 취소
            recommendList.removeIf(u -> u.getUserSeq().equals(user.getUserSeq())); //userSeq로 찾아서 제거
        } else {
            // 추천 안했으면 추가
            recommendList.add(user);
        }
        // 저장
        mjboardRepository.save(mjboard);
        // 현재 추천 수 반환 (프론트에 추천 수 표시할 때 사용)
        return recommendList.size();
    }



}


