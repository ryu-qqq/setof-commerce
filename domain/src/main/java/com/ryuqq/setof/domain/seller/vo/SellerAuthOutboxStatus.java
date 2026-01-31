package com.ryuqq.setof.domain.seller.vo;

/**
 * 셀러 인증 Outbox 상태.
 *
 * <p>인증 서버 연동 요청의 처리 상태를 나타냅니다.
 */
public enum SellerAuthOutboxStatus {

    /** 대기 중. 아직 처리되지 않은 상태. */
    PENDING("대기"),

    /** 처리 중. 현재 인증 서버 호출 진행 중. */
    PROCESSING("처리중"),

    /** 완료. 인증 서버 연동 성공. */
    COMPLETED("완료"),

    /** 실패. 최대 재시도 횟수 초과. */
    FAILED("실패");

    private final String description;

    SellerAuthOutboxStatus(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isProcessing() {
        return this == PROCESSING;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isFailed() {
        return this == FAILED;
    }

    public boolean canProcess() {
        return this == PENDING || this == PROCESSING;
    }
}
