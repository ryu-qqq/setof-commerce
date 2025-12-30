package com.ryuqq.setof.domain.qna.vo;

public enum WriterType {
    MEMBER("회원"),
    GUEST("비회원");

    private final String description;

    WriterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMember() {
        return this == MEMBER;
    }

    public boolean isGuest() {
        return this == GUEST;
    }
}
