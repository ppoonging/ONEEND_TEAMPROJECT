package com.springboot.biz.customer.question;

import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final HUserSerevice hUserSerevice;


    @GetMapping("/")
    public String getAllCustomer(Model model){
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers",customers);
        return "customer/customer_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{custSeq}")
    public String updateCustomerState(@PathVariable Integer custSeq, @RequestParam CustState state, Model model) {
        customerService.updateCustomerState(custSeq, state);
        Customer updatedCustomer = customerService.getCustomer(custSeq);
        model.addAttribute("customer", updatedCustomer); // 최신 상태 반영

        return "redirect:/customer/"; // 목록으로 이동
    }



    @GetMapping("/add")
    public String showAddForm(Model model) {
        Integer userSeq = hUserSerevice.getLoggedInUserSeq(); // 현재 로그인된 사용자 ID
        model.addAttribute("userSeq", userSeq);
        return "customer/customer_form";

    }


    @PostMapping("/add")
    public String addCustomer(@RequestParam String custTitle,
                              @RequestParam String custContent,
                              @RequestParam Integer userSeq
                             )  {



        customerService.createCustomer(custTitle, custContent, userSeq);
        return "redirect:/customer/"; // 등록 후 문의 목록으로 이동
    }

    @GetMapping("/detail/{custSeq}")
    public String custDetail(Model model , @PathVariable("custSeq") Integer custSeq){

        Customer customer = this.customerService.getCustomer(custSeq);

        model.addAttribute("customer",customer);
        return "customer/customer_detail";

    }




}
