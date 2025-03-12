package com.springboot.biz.mj.answer;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.user.HUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MjAnsService {
    private  final  MjAnsRepository mjAnsRepository;

    // 댓글 저장
    public MjAnswer createMjAnswer(Mjboard mjboard, String mjAnsContent, HUser user) {
        MjAnswer ans = new MjAnswer();
        ans.setMjBoard(mjboard);
        ans.setMjAnsContent(mjAnsContent);
        ans.setUserId(user); //유저 연결 필수
        ans.setMjAnsRegDate(LocalDateTime.now());
        ans.setMjAnsRecommend(0);
        return mjAnsRepository.save(ans);
    }

    // 대댓글 저장
    public MjAnswer createMjAnswer(Mjboard mjboard, String mjAnsContent, MjAnswer parentAnswer, HUser user) {
        MjAnswer ans = new MjAnswer();
        ans.setMjBoard(mjboard);
        ans.setMjAnsContent(mjAnsContent);
        ans.setUserId(user); //유저 연결 필수
        ans.setMjAnsComment(parentAnswer);
        ans.setMjAnsRegDate(LocalDateTime.now());
        ans.setMjAnsRecommend(0);
        return mjAnsRepository.save(ans);
    }



    public MjAnswer getMjAnswer(Integer mjAnsSeq) {
        Optional<MjAnswer> ans = this.mjAnsRepository.findById(mjAnsSeq);
        if (ans.isPresent()) {
            return ans.get();
        }else {
            throw new DataNotFoundException("객체가 없어요..");
        }
    }
    public void mjAnsmodify(MjAnswer mjAnswer, String mjAnsContent) {
        mjAnswer.setMjAnsContent(mjAnsContent);
        mjAnswer.setMjAnsRegDate(LocalDateTime.now());
        this.mjAnsRepository.save(mjAnswer);
    }
    public void deleteMjAnswer(MjAnswer mjAnswer) {
        this.mjAnsRepository.delete(mjAnswer);
    }
    public void mjAnsRecommend(MjAnswer mjAnswer) {
        mjAnswer.setMjAnsRecommend(mjAnswer.getMjAnsRecommend() +1);
        this.mjAnsRepository.save(mjAnswer);
    }


}
