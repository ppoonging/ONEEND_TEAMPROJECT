package com.springboot.biz;


import com.springboot.biz.free.board.FreeQuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MatgwangApplicationTests {



    @Autowired
    private FreeQuestionRepository freeQuestionRepository;



    @Test
    void contextLoads() {

        /*FreeQuestion f = new FreeQuestion();
        f.setFrboTitle("테스트 제목입니다");
        f.setFrboContent("테스트 내용입니다");
        f.setFrboRegDate(LocalDateTime.now());
        this.freeQuestionRepository.save(f);
*/


    }

}
