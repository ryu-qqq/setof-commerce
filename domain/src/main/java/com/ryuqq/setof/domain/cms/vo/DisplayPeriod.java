package com.ryuqq.setof.domain.cms.vo;

import java.time.Instant;

/**
 * 노출 기간 Value Object
 *
 * <p>시작일시와 종료일시로 구성된 기간을 나타냅니다.
 *
 * @param startDate 노출 시작 일시
 * @param endDate 노출 종료 일시
 * @author development-team
 * @since 1.0.0
 */
public record DisplayPeriod(Instant startDate, Instant endDate) {

    /** Compact Constructor */
    public DisplayPeriod {
        if (startDate == null) {
            throw new IllegalArgumentException("노출 시작일시는 필수입니다");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("노출 종료일시는 필수입니다");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("종료일시는 시작일시보다 이후여야 합니다");
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @return DisplayPeriod 인스턴스
     */
    public static DisplayPeriod of(Instant startDate, Instant endDate) {
        return new DisplayPeriod(startDate, endDate);
    }

    /**
     * 현재 노출 가능 여부 확인
     *
     * @param now 현재 시각
     * @return 노출 가능하면 true
     */
    public boolean isDisplayableAt(Instant now) {
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    /**
     * 노출 기간이 지났는지 확인
     *
     * @param now 현재 시각
     * @return 만료되었으면 true
     */
    public boolean isExpiredAt(Instant now) {
        return now.isAfter(endDate);
    }

    /**
     * 아직 노출 전인지 확인
     *
     * @param now 현재 시각
     * @return 노출 전이면 true
     */
    public boolean isBeforeDisplayAt(Instant now) {
        return now.isBefore(startDate);
    }
}
