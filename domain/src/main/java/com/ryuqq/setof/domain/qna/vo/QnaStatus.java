package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaStatus - Q&A 상태 Enum VO.
 *
 * <p>DOM-VO-002: Enum VO displayName() 제공.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum QnaStatus {

    PENDING("답변 대기"),
    ANSWERED("답변 완료"),
    CLOSED("종료");

    private final String displayName;

    QnaStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isAnswered() {
        return this == ANSWERED;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
