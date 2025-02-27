package com.springboot.biz.user;

import lombok.Getter;

@Getter
public enum HUserRole {
ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private HUserRole(String value){
        this.value= value;
    }
    private String value;
}
