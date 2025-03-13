package com.springboot.biz.mj.board;

import com.springboot.biz.DataNotFoundException;
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

    // 게시판 리스트 (추천수, 별점 계산 포함)
    public Map<String, Object> getList(Pageable pageable, String kw) {
        Page<Mjboard> paging = mjboardRepository.findAllByKeyword(kw, pageable);
        Map<Integer, Integer> starCountMap = new HashMap<>();
        for (Mjboard board : paging) {
            int recommendCount = board.getRecommendUsers().size();
            int starCount = (int) Math.min(5, Math.ceil((recommendCount / 50.0) * 5));
            starCountMap.put(board.getMjSeq(), starCount);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("paging", paging);
        result.put("starCountMap", starCountMap);
        return result;
    }

    // 게시글 작성
    public void create(String mjTitle, String mjContent, MultipartFile file, HUser hUser, Integer mjCnt) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";
        UUID uuid = UUID.randomUUID();
        String mjFileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, mjFileName);
        file.transferTo(saveFile);

        /*String thumbnailFileName = "thumb_" + mjFileName;
        File thumbnailFile = new File(projectPath, thumbnailFileName);
        mjthumbnailService.createThumbnail(saveFile, thumbnailFile);*/

        Mjboard mj = new Mjboard();
        mj.setMjFilePath("/files/mj/" + mjFileName);
        mj.setMjFileName(mjFileName);
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

    // 게시글 조회 (조회수 증가)
    @Transactional
    public Mjboard getMjboard(Integer mjSeq) {
        Mjboard mjboard = mjboardRepository.findById(mjSeq)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
        mjboard.setMjCnt(Optional.ofNullable(mjboard.getMjCnt()).orElse(0) + 1);
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
}
