package com.ryuqq.setof.application.legacy.mileage.dto.response;

import java.util.List;

/**
 * 레거시 Mileage 페이지 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * <p>마일리지 요약 정보와 이력 목록을 함께 반환.
 *
 * @param userMileage 사용자 마일리지 요약
 * @param histories 마일리지 이력 목록
 * @param pageNumber 현재 페이지 번호
 * @param pageSize 페이지 크기
 * @param totalElements 전체 요소 개수
 * @param totalPages 전체 페이지 개수
 * @param first 첫 페이지 여부
 * @param last 마지막 페이지 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyMileagePageResult(
        LegacyUserMileageResult userMileage,
        List<LegacyMileageHistoryResult> histories,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {

    public static LegacyMileagePageResult of(
            LegacyUserMileageResult userMileage,
            List<LegacyMileageHistoryResult> histories,
            int pageNumber,
            int pageSize,
            long totalElements) {

        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        boolean first = pageNumber == 0;
        boolean last = pageNumber >= totalPages - 1;

        return new LegacyMileagePageResult(
                userMileage,
                histories,
                pageNumber,
                pageSize,
                totalElements,
                totalPages,
                first,
                last);
    }

    public static LegacyMileagePageResult empty(long userId, int pageNumber, int pageSize) {
        return new LegacyMileagePageResult(
                LegacyUserMileageResult.empty(userId),
                List.of(),
                pageNumber,
                pageSize,
                0L,
                0,
                true,
                true);
    }

    public boolean isEmpty() {
        return histories == null || histories.isEmpty();
    }
}
