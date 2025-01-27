package com.springboot.biz.free.board;


import com.springboot.biz.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FreeQuestionService {

    private final FreeQuestionRepository freeQuestionRepository;

    //페이지 네이션 적용  차후에 검색까지 적용해야함
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




}
