package com.springboot.biz.customer.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 모든 고객 문의 조회
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 특정 고객 문의 조회
    public Optional<Customer> getCustomerById(Integer custSeq) {
        return customerRepository.findById(custSeq);
    }

    // 고객 문의 저장 (신규 등록 or 업데이트)
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void updateCustomerState(Integer custSeq, CustState state) {
        Customer customer = customerRepository.findById(custSeq)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));
        customer.setCustState(state);
        customerRepository.save(customer);

    }
}
