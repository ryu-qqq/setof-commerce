package com.ryuqq.setof.application.mileage.dto.query;

import java.util.List;

/**
 * MileageHistorySearchParams - 마일리지 이력 검색 요청 파라미터 DTO.
 *
 * <p>APP-DTO-003: Query DTO는 *Params 또는 *Query 네이밍.
 *
 * @param userId 사용자 ID
 * @param reasons 마일리지 사유 필터 (SAVE, USE, REFUND, EXPIRED)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MileageHistorySearchParams(long userId, List<String> reasons, int page, int size) {

    public static MileageHistorySearchParams of(
            long userId, List<String> reasons, int page, int size) {
        return new MileageHistorySearchParams(userId, reasons, page, size);
    }
}
