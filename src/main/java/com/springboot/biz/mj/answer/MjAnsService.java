package com.springboot.biz.mj.answer;


import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.user.HUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MjAnsService {
    private final  MjAnswerRepository mjAnswerRepository;

    public MjAnswer mjregdateAns(Mjboard mjboard, String mjAnscontent, HUser userId) {
        MjAnswer mjAnswer = new MjAnswer();
       mjAnswer.setMjAnsRegDate(LocalDateTime.now());
       mjAnswer.setUserId(userId);
       mjAnswer.setMjAnsContent(mjAnscontent);
       mjAnswer.setMjBoard(mjboard);
       this.mjAnswerRepository.save(mjAnswer);
        return mjAnswer;
    }
    public MjAnswer getMjAnswer(Integer mjAnsSeq){
        Optional<MjAnswer> mjAnswer = mjAnswerRepository.findById(mjAnsSeq);
        if (mjAnswer.isPresent()) {
            return mjAnswer.get();
        }else {
            throw new DataNotFoundException("객체가 없습니다.");
        }
    }
    public void modify(MjAnswer mjAnswer, String mjAnsContent) {
        mjAnswer.setMjAnsContent(mjAnsContent);
        mjAnswer.setMjAnsRegDate(LocalDateTime.now());
        this.mjAnswerRepository.save(mjAnswer);
    }

}
