package com.springboot.biz.free.board;


import com.springboot.biz.DataNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FreeQuestionService {

    private final FreeQuestionRepository freeQuestionRepository;
    private final FreeThumbnailService freeThumbnailService;


    /*   //페이지 네이션 적용  차후에 검색까지 적용해야함
       public Page<FreeQuestion> getFreeQuestionList(int Page) {
           List<Sort.Order> sorts = new ArrayList<>();
           sorts.add(Sort.Order.desc("freeQRegDate"));
           Pageable pageable = PageRequest.of(Page, 10, Sort.by(sorts));
           return this.freeQuestionRepository.findAll(pageable);
       }
       //Id 값 대신 Seq로 찾아갈 예정
       public FreeQuestion getFreeQuestion(Integer seq) {
           Optional<FreeQuestion> FQ = this.freeQuestionRepository.findById(seq);
           if(FQ.isPresent()) {
               return FQ.get();
           }else {
               throw new DataNotFoundException("자유게시판 데이터 없습니다.");
           }

       }
   */
    //리스트
    public Page<FreeQuestion> getFreeQuestionList(int page, String kw, String searchType) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("frboRegDate")));  // 내림차순 정렬

        if ("title".equals(searchType)) {
            return freeQuestionRepository.findAllByTitle(kw, pageable);  // 제목으로만 검색
        } else if ("content".equals(searchType)) {
            return freeQuestionRepository.findAllByContent(kw, pageable);  // 내용으로만 검색
        } else {  // "both"는 제목+내용으로 검색
            return freeQuestionRepository.findAllByTitleAndContent(kw, kw, pageable);
        }
    }

    // 추천순
    public Page<FreeQuestion> getPopularQuestionList(int page, String kw, String searchType) {
        Pageable pageable = PageRequest.of(page, 10);
        return freeQuestionRepository.findAllByRecommend(kw, searchType, pageable);
    }


    //글 등록,파일 업로드
    public void freeForm(String frboTitle, String frboContent, MultipartFile file , HUser hUser) throws Exception{
        //파일업로드
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";



        UUID uuid = UUID.randomUUID();
        String frboFileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath,frboFileName);
        file.transferTo(saveFile);



        //데이터 저장
        FreeQuestion f = new FreeQuestion();
        f.setFrboFilePath("/files/" + frboFileName);
        f.setFrboFileName(frboFileName);
        f.setFrboTitle(frboTitle);
        f.setFrboContent(frboContent);
        f.setFrboRegDate(LocalDateTime.now());
        f.setFreeAuthor(hUser);
        this.freeQuestionRepository.save(f);




    }
    public FreeQuestion getFreeQuestion(Integer foboSeq){
        Optional<FreeQuestion> freeQuestion = this.freeQuestionRepository.findById(foboSeq);

        if(freeQuestion.isPresent()){
            return freeQuestion.get();
        }else {
            throw new DataNotFoundException("검색한 데이타가 없습니다");
        }

    }


    public void  modify(FreeQuestion question,String frboTitle,String frboContent){

        question.setFrboTitle(frboTitle);
        question.setFrboContent(frboContent);
        question.setFrboModifyDate(LocalDateTime.now());
        this.freeQuestionRepository.save(question);


    }

    public void delete(FreeQuestion question){
        this.freeQuestionRepository.delete(question);
    }


    @Transactional
    public void toggleRecommend(FreeQuestion question, HUser user) {
        if (question.getFreeCnt().contains(user)) {
            // 이미 추천한 경우 -> 추천 취소
            question.getFreeCnt().remove(user);
        } else {
            // 추천하지 않은 경우 -> 추천
            question.getFreeCnt().add(user);
        }
        freeQuestionRepository.save(question); // 저장
    }






}
