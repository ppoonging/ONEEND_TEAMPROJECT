package com.springboot.biz;

import com.springboot.biz.freequestion.FreeQuestion;
import com.springboot.biz.freequestion.FreeQuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class MatgwoungApplicationTests {



    @Autowired
    private FreeQuestionRepository freeQuestionRepository;



    @Test
    void contextLoads() {

        FreeQuestion f = new FreeQuestion();
        f.setFreeQTitle("테스트 제목입니다");
        f.setFreeQContent("테스트 내용입니다");
        f.setFreeQRegDate(LocalDateTime.now());
        this.freeQuestionRepository.save(f);



    }

}
