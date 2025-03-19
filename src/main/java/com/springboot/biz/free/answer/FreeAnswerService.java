package com.springboot.biz.free.answer;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.free.board.FreeQuestion;
import com.springboot.biz.user.HUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FreeAnswerService {

    private final FreeAnswerRepository freeAnswerRepository;

    //답변 생성
    public FreeAnswer freeAnswerCreate(FreeQuestion freeQuestion, String frboAnsContent, FreeAnswer parentAnswer, HUser author) {
        FreeAnswer freeAnswer = new FreeAnswer();
        freeAnswer.setFrboAnsContent(frboAnsContent); // 댓글 내용
        freeAnswer.setFrboARegDate(LocalDateTime.now()); // 현재 시간 저장
        freeAnswer.setFreeQuestion(freeQuestion); // 해당 게시글(질문) 연결
        freeAnswer.setFrboAnsAuthor(author);//작성자

        //대댓글일 경우 부모 댓글 연결
        if (parentAnswer != null) {
            freeAnswer.setParentAnswer(parentAnswer);
        }

        freeAnswerRepository.save(freeAnswer);
        return freeAnswer;
    }

    //대댓글 생성
    public FreeAnswer createReply(FreeQuestion question, String content, FreeAnswer parent, HUser author) {
        FreeAnswer reply = new FreeAnswer();
        reply.setFreeQuestion(question);
        reply.setFrboAnsContent(content);
        reply.setFrboAnsAuthor(author);
        reply.setParentAnswer(parent);
        reply.setFrboARegDate(LocalDateTime.now());
        return freeAnswerRepository.save(reply);
    }


    //댓글 조회
    public FreeAnswer getFreeAnswer(Integer frboAnSeq) {
        return freeAnswerRepository.findById(frboAnSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + frboAnSeq));
    }

    //댓글 수정
    @Transactional
    public void modify(FreeAnswer freeAnswer, String frboAnsContent) {
        freeAnswer.setFrboAnsContent(frboAnsContent);
        freeAnswer.setFrboAnsModify(LocalDateTime.now());
        freeAnswerRepository.save(freeAnswer);
    }

    //댓글 삭제
    public void delete(FreeAnswer freeAnswer) {
        freeAnswerRepository.delete(freeAnswer);
    }


}