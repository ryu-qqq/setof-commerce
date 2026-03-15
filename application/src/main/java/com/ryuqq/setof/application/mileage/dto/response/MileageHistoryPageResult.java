package com.ryuqq.setof.application.mileage.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.util.List;

/**
 * MileageHistoryPageResult - 마일리지 이력 페이지 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * <p>마일리지 요약 정보와 이력 목록, 페이지 메타를 함께 반환.
 *
 * @param userId 사용자 ID
 * @param mileageSummary 마일리지 요약 정보
 * @param histories 마일리지 이력 목록
 * @param pageMeta 페이지 메타 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MileageHistoryPageResult(
        long userId,
        MileageSummary mileageSummary,
        List<MileageHistoryItemResult> histories,
        PageMeta pageMeta) {

    public static MileageHistoryPageResult of(
            long userId,
            MileageSummary mileageSummary,
            List<MileageHistoryItemResult> histories,
            PageMeta pageMeta) {
        return new MileageHistoryPageResult(userId, mileageSummary, histories, pageMeta);
    }

    public static MileageHistoryPageResult empty(long userId, int size) {
        return new MileageHistoryPageResult(
                userId, MileageSummary.empty(), List.of(), PageMeta.empty(size));
    }
}
