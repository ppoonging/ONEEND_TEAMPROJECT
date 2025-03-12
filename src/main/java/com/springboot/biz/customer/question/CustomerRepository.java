package com.springboot.biz.customer.question;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Integer custSeq(Integer custSeq);
}
