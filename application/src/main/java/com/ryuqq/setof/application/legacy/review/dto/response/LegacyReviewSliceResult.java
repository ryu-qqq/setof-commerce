package com.ryuqq.setof.application.legacy.review.dto.response;

import java.util.List;

/**
 * 레거시 Review 슬라이스 결과 DTO.
 *
 * <p>커서 기반 페이징용 (CustomSlice 대응).
 *
 * @param content 리뷰 목록
 * @param hasNext 다음 페이지 존재 여부
 * @param totalElements 총 요소 수
 * @param lastDomainId 마지막 도메인 ID (커서)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyReviewSliceResult<T>(
        List<T> content, boolean hasNext, long totalElements, Long lastDomainId) {

    public static <T> LegacyReviewSliceResult<T> of(
            List<T> content,
            int pageSize,
            long totalElements,
            java.util.function.Function<T, Long> idExtractor) {
        boolean hasNext = content.size() > pageSize;
        List<T> resultContent = hasNext ? content.subList(0, pageSize) : content;

        Long lastDomainId =
                resultContent.isEmpty()
                        ? null
                        : idExtractor.apply(resultContent.get(resultContent.size() - 1));

        return new LegacyReviewSliceResult<>(resultContent, hasNext, totalElements, lastDomainId);
    }
}
