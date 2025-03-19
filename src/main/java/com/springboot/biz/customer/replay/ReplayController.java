package com.springboot.biz.customer.replay;

import com.springboot.biz.customer.question.Customer;
import com.springboot.biz.customer.question.CustomerService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/replay")
public class ReplayController {

    private final CustomerService customerService;
    private final ReplayService replayService;
    private final HUserSerevice hUserSerevice;


    //답변등록
    @PostMapping("/create/{custSeq}") // /replay/create/{custSeq} 경로에서 답변 등록
    public String createReplay(@PathVariable("custSeq") Integer custSeq,
                               @RequestParam String replayContent,
                               Model model) {

        // 🔹 현재 로그인한 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // 사용자명 가져오기
        HUser user = hUserSerevice.getUserByUsername(username); // 사용자 정보 조회

        Customer customer = customerService.getCustomer(custSeq);
        replayService.replayCreate(customer, user, replayContent);


        model.addAttribute("customer", customer);
        model.addAttribute("replays", replayService.getReplaysByCustomer(custSeq));

        return "customer/customer_detail";
    }
}

