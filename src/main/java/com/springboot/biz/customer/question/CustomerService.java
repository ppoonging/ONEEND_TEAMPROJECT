package com.springboot.biz.customer.question;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private  final HUserSerevice hUserSerevice;


    // 전체 고객 문의 조회
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 특정 고객 문의 조회 (예외처리 포함)
    public Customer getCustomer(Integer custSeq) {
        return customerRepository.findById(custSeq)
                .orElseThrow(() -> new DataNotFoundException("검색한 데이터가 없습니다"));
    }

    // 고객 문의 등록
    public void createCustomer(String custTitle, String custContent, Integer userSeq){


        // 고객 정보 저장
        HUser user = hUserSerevice.getUserById(userSeq);
        Customer customer = new Customer();
        customer.setCustTitle(custTitle);
        customer.setCustContent(custContent);
        customer.setUser(user);
        customer.setCustRegTime(LocalDateTime.now());
        customer.setCustState(CustState.PENDING);  // 기본값: 대기중
        customerRepository.save(customer);
    }

    // 문의 상태 변경
    public void updateCustomerState(Integer custSeq, CustState state) {
        Customer customer = customerRepository.findById(custSeq)
                .orElseThrow(() -> new DataNotFoundException("문의가 존재하지 않습니다."));
        customer.setCustState(state);
        customerRepository.save(customer);
        customerRepository.flush(); // 즉시 반영
    }
    }









