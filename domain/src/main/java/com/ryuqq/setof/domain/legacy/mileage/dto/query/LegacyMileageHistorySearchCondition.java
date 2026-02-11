package com.ryuqq.setof.domain.legacy.mileage.dto.query;

import java.util.List;

/**
 * 레거시 MileageHistory 검색 조건 DTO.
 *
 * <p>fetchMileageHistories 검색 조건.
 *
 * @param userId 사용자 ID
 * @param reasons 마일리지 사유 필터 (SAVE, USE, REFUND, EXPIRED)
 * @param pageNumber 페이지 번호 (0부터 시작)
 * @param pageSize 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyMileageHistorySearchCondition(
        long userId, List<String> reasons, int pageNumber, int pageSize) {

    public static LegacyMileageHistorySearchCondition of(
            long userId, List<String> reasons, int pageNumber, int pageSize) {
        return new LegacyMileageHistorySearchCondition(userId, reasons, pageNumber, pageSize);
    }

    public static LegacyMileageHistorySearchCondition ofUser(
            long userId, int pageNumber, int pageSize) {
        return new LegacyMileageHistorySearchCondition(userId, null, pageNumber, pageSize);
    }

    public boolean hasReasons() {
        return reasons != null && !reasons.isEmpty();
    }

    public long getOffset() {
        return (long) pageNumber * pageSize;
    }
}
