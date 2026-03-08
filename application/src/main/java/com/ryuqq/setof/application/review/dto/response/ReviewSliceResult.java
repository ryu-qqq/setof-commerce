package com.ryuqq.setof.application.review.dto.response;

import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;

/**
 * 리뷰 커서 기반 슬라이스 결과 DTO.
 *
 * <p>SliceMeta를 포함하는 표준 슬라이스 결과입니다.
 *
 * @param results 리뷰 결과 목록
 * @param sliceMeta 슬라이스 메타 정보
 * @param totalElements 전체 요소 수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewSliceResult(
        List<ReviewResult> results, SliceMeta sliceMeta, long totalElements) {

    public static ReviewSliceResult of(
            List<ReviewResult> results,
            int size,
            boolean hasNext,
            Long lastReviewId,
            long totalElements) {
        SliceMeta sliceMeta = SliceMeta.withCursor(lastReviewId, size, hasNext, results.size());
        return new ReviewSliceResult(results, sliceMeta, totalElements);
    }

    public static ReviewSliceResult empty(int size) {
        return new ReviewSliceResult(List.of(), SliceMeta.empty(size), 0L);
    }
}
