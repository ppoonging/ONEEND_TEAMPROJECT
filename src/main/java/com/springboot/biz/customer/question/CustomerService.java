package com.springboot.biz.customer.question;

import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private  final HUserSerevice hUserSerevice;


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

        System.out.println("현재 상태: " + customer.getCustState());
        System.out.println("변경할 상태: " + state);

        customer.setCustState(state);
        customerRepository.save(customer);
        customerRepository.flush(); // 변경 사항 즉시 반영

        System.out.println("변경된 상태: " + customer.getCustState());
    }




    public void createCustomer(String custTitle, String custContent, Integer userSeq) {
        HUser user = hUserSerevice.getUserById(userSeq);

        Customer customer = new Customer();
        customer.setCustTitle(custTitle);
        customer.setCustContent(custContent);
        customer.setUser(user);
        customer.setCustRegTime(LocalDateTime.now());
        customer.setCustState(CustState.PENDING); // 기본값: 대기중


        customerRepository.save(customer);
    }

    public Customer getCustomer(Integer custSeq){

        Optional<Customer> customer = this.customerRepository.findById(custSeq);
        if(customer.isPresent()){

            return customer.get();
        }else {
            throw new DataNotFoundException("검색한 데이타가 없습니다");
        }
    }








}
