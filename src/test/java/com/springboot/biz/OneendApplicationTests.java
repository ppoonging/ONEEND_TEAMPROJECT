package com.springboot.biz;


import com.springboot.biz.free.board.FreeQuestion;
import com.springboot.biz.free.board.FreeQuestionRepository;
import com.springboot.biz.free.board.FreeQuestionService;
import com.springboot.biz.notion.MgNotion;
import com.springboot.biz.notion.MgRepository;
import com.springboot.biz.user.HUserSerevice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class OneendApplicationTests {



    @Autowired

    private FreeQuestionService freeQuestionService;
    private HUserSerevice hUserSerevice;


    @Test
    void contextLoads() {






    }

}
