package com.springboot.biz.customer.question;

import com.springboot.biz.customer.replay.Replay;
import com.springboot.biz.customer.replay.ReplayService;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final HUserSerevice hUserSerevice;
    private final ReplayService replayService;

    //고객센터 리스트
    @GetMapping("/")
    public String getAllCustomer(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/customer_list";
    }

    // 문의 작성 폼
    @GetMapping("/add")
    public String showAddForm(Model model, Principal principal) {
        String username = principal.getName(); // 로그인한 사용자명
        HUser user = hUserSerevice.getUserByUsername(username);
        model.addAttribute("userSeq", user.getUserSeq()); // 사용자 PK 전달
        return "customer/customer_form";
    }


    // 문의 등록
    @PostMapping("/add")
    public String addCustomer(@RequestParam String custTitle,
                              @RequestParam String custContent,
                              Principal principal) {
        String username = principal.getName(); // 로그인 사용자
        HUser user = hUserSerevice.getUserByUsername(username);
        customerService.createCustomer(custTitle, custContent, user.getUserSeq());
        return "redirect:/customer/";
    }
     
    //문의 상세보기
    @GetMapping("/detail/{custSeq}")
    public String custDetail(Model model, @PathVariable("custSeq") Integer custSeq) {
        Customer customer = customerService.getCustomer(custSeq);
        List<Replay> replays = replayService.getReplaysByCustomer(custSeq); // 답변 목록 조회

        model.addAttribute("customer", customer);
        model.addAttribute("replays", replays); // 답변 목록 추가

        return "customer/customer_detail";
    }

    // 문의 상태 변경 (관리자만 가능)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{custSeq}")
    public String updateCustomerState(@PathVariable Integer custSeq, @RequestParam CustState state) {
        customerService.updateCustomerState(custSeq, state);
        return "redirect:/customer/";
    }

}
