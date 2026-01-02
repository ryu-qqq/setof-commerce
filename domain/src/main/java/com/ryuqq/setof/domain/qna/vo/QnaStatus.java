package com.ryuqq.setof.domain.qna.vo;

public enum QnaStatus {

    OPEN("답변 대기"),
    CLOSED("답변 완료");

    private final String description;

    QnaStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
