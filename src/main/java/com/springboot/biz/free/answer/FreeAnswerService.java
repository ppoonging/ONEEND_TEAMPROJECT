package com.springboot.biz.free.answer;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.free.board.FreeQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FreeAnswerService {

    private final FreeAnswerRepository freeAnswerRepository;


    public FreeAnswer freeAnswerCreate(FreeQuestion freeQuestion, String frboAnsContent,FreeAnswer  parentAnswer) {
        FreeAnswer freeAnswer = new FreeAnswer();
        freeAnswer.setFrboAnsContent(frboAnsContent); // 댓글 내용
        freeAnswer.setFrboARegDate(LocalDateTime.now()); // 현재 시간 저장
        freeAnswer.setFreeQuestion(freeQuestion); // 해당 게시글(질문) 연결
        freeAnswer.setParentAnswer(parentAnswer); //부모댓글

        return freeAnswerRepository.save(freeAnswer); // 저장 후 반환gggggggg
    }

    // 🔹 댓글 조회 (ID로 찾기)
    public FreeAnswer getFreeAnswer(Integer frboAnSeq) {
        return freeAnswerRepository.findById(frboAnSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + frboAnSeq));
    }

    // 🔹 댓글 수정
    @Transactional
    public void modify(FreeAnswer freeAnswer, String frboAnsContent) {
        freeAnswer.setFrboAnsContent(frboAnsContent); // 내용 변경
        freeAnswer.setFrboAnsModify(LocalDateTime.now()); // 수정 시간 업데이트
        freeAnswerRepository.save(freeAnswer); // 저장
    }


    public void delete(FreeAnswer freeAnswer) {
        freeAnswerRepository.delete(freeAnswer);
    }







}