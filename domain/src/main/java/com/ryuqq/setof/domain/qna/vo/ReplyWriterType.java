package com.ryuqq.setof.domain.qna.vo;

public enum ReplyWriterType {
    SELLER("판매자"),
    CUSTOMER("고객"),
    ADMIN("관리자");

    private final String description;

    ReplyWriterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSeller() {
        return this == SELLER;
    }

    public boolean isCustomer() {
        return this == CUSTOMER;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
