package com.ryuqq.setof.domain.refund.vo;

import java.time.Instant;

/**
 * 반품 보류 정보 Value Object.
 *
 * <p>보류 사유와 보류 시각을 포함합니다.
 *
 * @param holdReason 보류 사유 (필수)
 * @param heldAt 보류 시각 (필수)
 */
public record HoldInfo(String holdReason, Instant heldAt) {

    public HoldInfo {
        if (holdReason == null || holdReason.isBlank()) {
            throw new IllegalArgumentException("보류 사유는 필수입니다");
        }
        if (heldAt == null) {
            throw new IllegalArgumentException("보류 시각은 필수입니다");
        }
    }

    /**
     * 보류 정보 생성.
     *
     * @param holdReason 보류 사유
     * @param heldAt 보류 시각
     * @return HoldInfo 인스턴스
     */
    public static HoldInfo of(String holdReason, Instant heldAt) {
        return new HoldInfo(holdReason, heldAt);
    }
}
