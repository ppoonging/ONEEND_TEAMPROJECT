package com.springboot.biz.notion;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.user.MgUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MgService {

    private final MgRepository mgRepository;

    public Page<MgNotion> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("notionRegDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.mgRepository.findAll(pageable);
    }

    public MgNotion getMgNotion(Integer notionSeq) {
        Optional<MgNotion> mgNotion = this.mgRepository.findById(notionSeq);

        if(mgNotion.isPresent()) {
            return mgNotion.get();
        }else {
            throw new DataNotFoundException("검색한 데이터가 없습니다");
        }
    }

    public void create(String notionTitle, String notionContent, MgUser mgUser) {
        MgNotion q = new MgNotion();
        q.setNotionTitle(notionTitle);
        q.setNotionContent(notionContent);
        q.setNotionRegDate(LocalDateTime.now());
        q.setAuthor(mgUser);
        this.mgRepository.save(q);
    }

}
