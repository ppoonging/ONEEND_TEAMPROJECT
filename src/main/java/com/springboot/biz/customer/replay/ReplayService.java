package com.springboot.biz.customer.replay;

import com.springboot.biz.customer.question.Customer;
import com.springboot.biz.customer.question.CustomerRepository;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplayService {



    private final CustomerRepository customerRepository;
    private final HUserSerevice hUserSerevice;
    private final ReplayRepository replayRepository;

    // 특정 문의에 대한 답변 목록 조회
    public List<Replay> getReplaysByCustomer(Integer custSeq) {
        return replayRepository.findByCustomer_CustSeq(custSeq);
    }

    // 답변 생성
    public Replay replayCreate(Customer customer, HUser user, String replayContent) {
        Replay r = new Replay();
        r.setReplayContent(replayContent);
        r.setReplayRegTime(LocalDateTime.now());
        r.setCustomer(customer);
        r.setUser(user);

        return replayRepository.save(r);
    }


}
