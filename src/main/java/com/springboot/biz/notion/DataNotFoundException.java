package com.springboot.biz.notion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "객체가 없어요")
public class DataNotFoundException extends RuntimeException{

    private static final long seriaVersionUID = 1L;

    public DataNotFoundException(String message) {
        super(message);

    }

}
