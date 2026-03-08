package com.ryuqq.setof.application.review.dto.response;

import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;

/**
 * 작성 가능한 리뷰 커서 기반 페이징 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param content 주문 목록
 * @param sliceMeta 슬라이스 메타 정보
 * @param totalElements 전체 요소 수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record AvailableReviewSliceResult(
        List<AvailableReviewOrderResult> content, SliceMeta sliceMeta, long totalElements) {

    public static AvailableReviewSliceResult of(
            List<AvailableReviewOrderResult> content, SliceMeta sliceMeta, long totalElements) {
        return new AvailableReviewSliceResult(content, sliceMeta, totalElements);
    }

    public static AvailableReviewSliceResult empty() {
        return new AvailableReviewSliceResult(List.of(), SliceMeta.empty(), 0L);
    }

    /** 편의 접근자 - 다음 페이지 존재 여부. */
    public boolean hasNext() {
        return sliceMeta.hasNext();
    }
}
