package com.springboot.biz.mj.board;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.mj.board.MjboardRepository;
import com.springboot.biz.mj.board.MjthumbnailService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MjboardService {
    private final MjboardRepository mjboardRepository;
    private final MjthumbnailService mjthumbnailService;
    private final HUserSerevice hUserSerevice;

    // 검색 + 정렬 기능 포함된 리스트 조회 (중복 제거 및 구조 정리)
    public Map<String, Object> getList(Pageable pageable, String kw, String searchType, String sort) {
        Page<Mjboard> paging;

        // 검색 처리
        if (!kw.isEmpty()) {
            paging = mjboardRepository.findAllByKeyword(kw, pageable);
        } else {
            // 정렬 처리
            switch (sort) {
                case "popular":
                    paging = mjboardRepository.findAllByOrderByRecommendDesc(pageable);
                    break;
                case "view":
                    paging = mjboardRepository.findAllByOrderByViewDesc(pageable);
                    break;
                default:
                    paging = mjboardRepository.findAllByOrderByMjRegDateDesc(pageable);
                    break;
            }
        }

        // 별점 계산
        Map<Integer, Integer> starCountMap = new HashMap<>();
        for (Mjboard board : paging) {
            int recommendCount = board.getRecommendUsers().size();
            int starCount = (int) Math.min(5, Math.ceil((recommendCount / 50.0) * 5));
            starCountMap.put(board.getMjSeq(), starCount);
        }

        // 결과 반환
        Map<String, Object> result = new HashMap<>();
        result.put("paging", paging);
        result.put("starCountMap", starCountMap);
        return result;
    }

    // 게시글 작성
    public void create(String mjTitle, String mjContent, MultipartFile file, HUser hUser, Integer mjCnt,
                       String mjMapTitle, String mjMapAddress, String mjMapRodeAddress, Double mjMapLatitude,
                       Double mjMapLongitude, String mjMapLink, String mjMapCategory) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";
        UUID uuid = UUID.randomUUID();
        String mjFileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, mjFileName);
        file.transferTo(saveFile);

        Mjboard mj = new Mjboard();
        mj.setMjFilePath("/files/mj/" + mjFileName);
        mj.setMjFileName(mjFileName);
        mj.setMjTitle(mjTitle);
        mj.setMjContent(mjContent);
        mj.setMjRegDate(LocalDateTime.now());
        mj.setUserId(hUser);
        mj.setMjCnt(0);

        // 지도 정보 저장
        mj.setMjMapTitle(mjMapTitle);
        mj.setMjMapAddress(mjMapAddress);
        mj.setMjMapRodeAddress(mjMapRodeAddress);
        mj.setMjMapLatitude(mjMapLatitude);
        mj.setMjMapLongitude(mjMapLongitude);
        mj.setMjMapLink(mjMapLink);
        mj.setMjMapCategory(mjMapCategory);

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

    // 게시글 조회 (조회수 증가)
    @Transactional
    public Mjboard getMjboard(Integer mjSeq) {
        Mjboard mjboard = mjboardRepository.findById(mjSeq)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
        mjboard.setMjCnt(Optional.ofNullable(mjboard.getMjCnt()).orElse(0) + 1);
        return mjboard;
    }

    // 게시글 수정
    public void modify(Mjboard mjboard, String mjTitle, String mjContent, String mjMapTitle, String mjMapAddress, String mjMapRodeAddress, Double mjMapLatitude,
                       Double mjMapLongitude, String mjMapLink, String mjMapCategory) {

        mjboard.setMjTitle(mjTitle);
        mjboard.setMjContent(mjContent);

        //지도 정보 수정
        mjboard.setMjMapTitle(mjMapTitle);
        mjboard.setMjMapAddress(mjMapAddress);
        mjboard.setMjMapRodeAddress(mjMapRodeAddress);
        mjboard.setMjMapLatitude(mjMapLatitude);
        mjboard.setMjMapLongitude(mjMapLongitude);
        mjboard.setMjMapLink(mjMapLink);
        mjboard.setMjMapCategory(mjMapCategory);

        mjboardRepository.save(mjboard);
    }

    //게시글 삭제
    public void delete(Mjboard mjboard) {
        mjboardRepository.delete(mjboard);
    }

    // 추천 기능 (토글 방식)
    @Transactional
    public void mjRecommend(Mjboard mjboard, HUser user) {
        Set<HUser> recommendUsers = mjboard.getRecommendUsers();
        if (recommendUsers.contains(user)) {
            recommendUsers.remove(user);
        } else {
            recommendUsers.add(user);
        }
        mjboardRepository.save(mjboard);
    }

    // 모든 게시글 리스트 가져오기
    public List<Mjboard> getList() {
        return this.mjboardRepository.findAll();
    }

    // 조회수 기준 Top 9 게시글 가져오기
    public List<Mjboard> getTop9ByView() {
        return mjboardRepository.findTop9ByOrderByMjCntDesc();
    }
}
