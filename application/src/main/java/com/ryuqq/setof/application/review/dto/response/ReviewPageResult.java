package com.ryuqq.setof.application.review.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

/**
 * 상품그룹 리뷰 페이지 조회 결과 DTO.
 *
 * <p>PageMeta를 포함하는 표준 페이지 결과입니다. 평균 평점 포함.
 *
 * @param results 리뷰 결과 목록
 * @param pageMeta 페이징 메타 정보
 * @param averageRating 평균 평점
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewPageResult(
        List<ReviewResult> results, PageMeta pageMeta, double averageRating) {

    public static ReviewPageResult of(
            List<ReviewResult> results,
            int page,
            int size,
            long totalElements,
            double averageRating) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new ReviewPageResult(results, pageMeta, averageRating);
    }

    public static ReviewPageResult empty(int size) {
        return new ReviewPageResult(List.of(), PageMeta.empty(size), 0.0);
    }
}
