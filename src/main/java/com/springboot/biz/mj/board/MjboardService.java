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
import java.util.*;

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
    // 게시판 리스트 (추천수, 별점 계산 포함)
    public Map<String, Object> getList(Pageable pageable, String kw) {
        Page<Mjboard> paging = mjboardRepository.findAllByKeyword(kw, pageable); // 기존 페이징

        // 추천수, 별점 Map으로 저장
        Map<Integer, Integer> starCountMap = new HashMap<>();
        for (Mjboard board : paging) {
            int recommendCount = board.getRecommendUsers().size(); // 추천 수
            int starCount = (int) Math.min(5, Math.ceil((recommendCount / 50.0) * 5)); // 별점 계산
            starCountMap.put(board.getMjSeq(), starCount);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("paging", paging); // 원본 데이터
        result.put("starCountMap", starCountMap); // 별점 맵
        return result;
    }





    // 게시글 작성 (썸네일 URL 저장 X, 원본 파일명만 저장)
    public void create(String mjTitle, String mjContent, MultipartFile file, HUser hUser, Integer mjCnt) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";
        UUID uuid = UUID.randomUUID();
        String mjFileName = uuid + "_" + file.getOriginalFilename(); // 저장할 파일명
        File saveFile = new File(projectPath, mjFileName);
        file.transferTo(saveFile); // 원본 파일 저장

        // 썸네일 파일 자동 생성
        String thumbnailFileName = "thumb_" + mjFileName;
        File thumbnailFile = new File(projectPath, thumbnailFileName);
        mjthumbnailService.createThumbnail(saveFile, thumbnailFile); // 썸네일 생성

        // DB에는 원본 파일명만 저장
        Mjboard mj = new Mjboard();
        mj.setMjFilePath("/files/mj/" + mjFileName); // 원본 파일 경로
        mj.setMjFileName(mjFileName); // 원본 파일명
        mj.setMjTitle(mjTitle);
        mj.setMjContent(mjContent);
        mj.setMjRegDate(LocalDateTime.now());
        mj.setUserId(hUser);
        mj.setMjCnt(0);
        mjboardRepository.save(mj);
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
    @Transactional
    public Mjboard getMjboard(Integer mjSeq) {
        Mjboard mjboard = mjboardRepository.findById(mjSeq)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
        if (mjboard.getMjCnt() == null) {
            mjboard.setMjCnt(0);  // 혹시라도 null이면 0으로 세팅
        }
        mjboard.setMjCnt(mjboard.getMjCnt() + 1); // 조회수 +1
        return mjboard;
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
        Set<HUser> recommendUsers = mjboard.getRecommendUsers(); // 추천한 사용자 목록

        // 이미 추천했으면 추천 취소
        if (recommendUsers.contains(user)) {
            recommendUsers.remove(user);
        } else {
            recommendUsers.add(user); // 추천 안했으면 추가
        }

        mjboardRepository.save(mjboard); // 저장
        return recommendUsers.size(); // 추천 수 반환
    }

}


