package com.springboot.biz;


import com.springboot.biz.free.board.FreeQuestionRepository;
import com.springboot.biz.notion.MgNotion;
import com.springboot.biz.notion.MgRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class OneendApplicationTests {



    @Autowired

    private MgRepository mgRepository;



    @Test
    void contextLoads() {

        MgNotion m = new MgNotion();

        m.setNotionTitle("제목");
        m.setNotionContent("내용");
        m.setNotionRegDate(LocalDateTime.now());
        this.mgRepository.save(m);





    }

}
