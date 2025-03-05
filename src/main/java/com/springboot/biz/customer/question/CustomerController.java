package com.springboot.biz.customer.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping("/")
    public String getAllCustomer(Model model){
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers",customers);
        return "customer/customer_list";
    }

    @PostMapping("/update/{id}")
    public String updateCustomerState(@PathVariable Integer id, @RequestParam CustState state){
        customerService.updateCustomerState(id,state);
        return  "redirect:/customer/";
    }


}
