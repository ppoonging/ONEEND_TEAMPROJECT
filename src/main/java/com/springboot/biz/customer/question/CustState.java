package com.springboot.biz.customer.question;

public enum CustState {
    PENDING("대기중"),
    IN_PROGRESS("처리중"),
    COMPLETED("처리완료");

    private final String displayName;

    CustState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

