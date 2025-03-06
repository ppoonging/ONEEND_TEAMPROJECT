package com.springboot.biz.mj.board;


import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.user.HUser;
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
public class MjService {
    private final MjRepository mjRepository;

    public Page<Mjboard> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("mjRegDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.mjRepository.findAllByKeyword(kw, pageable);

    }
    public void mjregdate(String mjTitle, String mjContent, HUser userId) {
        Mjboard mj = new Mjboard();
        mj.setUserId(userId);
        mj.setMjTitle(mjTitle);
        mj.setMjContent(mjContent);
        mj.setMjRegDate(LocalDateTime.now());
        this.mjRepository.save(mj);

    }
    public Mjboard getMjboard(Integer mjseq) {
        Optional<Mjboard> mjboard = this.mjRepository.findById(mjseq);
        if (mjboard.isPresent()) {
            return mjboard.get();
        }else {
            throw new DataNotFoundException("객체가 없습니다.");
        }

    }
    public void modify(Mjboard mjboard, String mjTitle, String mjContent) {
        mjboard.setMjTitle(mjTitle);
        mjboard.setMjContent(mjContent);
        mjboard.setMjRegDate(LocalDateTime.now());
        this.mjRepository.save(mjboard);
    }

}
