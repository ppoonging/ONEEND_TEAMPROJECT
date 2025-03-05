package com.springboot.biz.root.rootUser;

import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RootAuthService {

    private final RootAuthRepository rootAuthRepository;
    private final RootRepository rootRepository;

    public List<Root> get() {
        return this.rootRepository.findAll();
    }

}
