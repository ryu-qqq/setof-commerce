package com.ryuqq.setof.domain.common.vo;

import java.time.Instant;

/**
 * DisplayPeriod - 노출 기간 Value Object.
 *
 * <p>시작일과 종료일을 관리하여 노출 기간을 표현합니다. Navigation, Banner, ContentPage, DisplayComponent 등 여러 도메인에서
 * 공유합니다.
 *
 * @param startDate 노출 시작일
 * @param endDate 노출 종료일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayPeriod(Instant startDate, Instant endDate) {

    public DisplayPeriod {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate must not be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("endDate must not be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
    }

    public static DisplayPeriod of(Instant startDate, Instant endDate) {
        return new DisplayPeriod(startDate, endDate);
    }

    /**
     * 주어진 시각이 노출 기간 내인지 확인합니다.
     *
     * @param now 확인할 시각
     * @return 노출 기간 내이면 true
     */
    public boolean isWithin(Instant now) {
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }
}
