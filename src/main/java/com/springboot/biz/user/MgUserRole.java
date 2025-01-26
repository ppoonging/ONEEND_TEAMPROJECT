package com.springboot.biz.user;

import lombok.Getter;

@Getter
public enum MgUserRole {
ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private MgUserRole(String value){
        this.value= value;
    }
    private String value;
}
