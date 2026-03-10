package com.ryuqq.setof.domain.mileage.query;

import java.util.List;

/**
 * MileageHistorySearchCriteria - 마일리지 이력 검색 기준.
 *
 * <p>마일리지 이력 페이지 조회 시 사용되는 도메인 검색 기준 객체.
 *
 * @param userId 사용자 ID
 * @param reasons 마일리지 사유 필터 (SAVE, USE, REFUND, EXPIRED)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MileageHistorySearchCriteria(long userId, List<String> reasons, int page, int size) {

    public static MileageHistorySearchCriteria of(
            long userId, List<String> reasons, int page, int size) {
        return new MileageHistorySearchCriteria(userId, reasons, page, size);
    }

    public boolean hasReasons() {
        return reasons != null && !reasons.isEmpty();
    }

    public long offset() {
        return (long) page * size;
    }
}
