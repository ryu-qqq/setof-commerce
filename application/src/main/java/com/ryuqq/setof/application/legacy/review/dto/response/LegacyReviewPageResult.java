package com.ryuqq.setof.application.legacy.review.dto.response;

import java.util.List;

/**
 * 레거시 Review 페이지 결과 DTO.
 *
 * <p>평균 평점 포함 (ReviewPage 대응).
 *
 * @param averageRating 평균 평점
 * @param content 리뷰 목록
 * @param totalPages 총 페이지 수
 * @param totalElements 총 리뷰 수
 * @param last 마지막 페이지 여부
 * @param first 첫 페이지 여부
 * @param number 현재 페이지 번호
 * @param size 페이지 크기
 * @param numberOfElements 현재 페이지 요소 수
 * @param empty 빈 페이지 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyReviewPageResult(
        double averageRating,
        List<LegacyReviewResult> content,
        int totalPages,
        long totalElements,
        boolean last,
        boolean first,
        int number,
        int size,
        int numberOfElements,
        boolean empty) {

    public static LegacyReviewPageResult of(
            double averageRating,
            List<LegacyReviewResult> content,
            int pageNumber,
            int pageSize,
            long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean first = pageNumber == 0;
        boolean last = pageNumber >= totalPages - 1;
        boolean empty = content.isEmpty();
        int numberOfElements = content.size();

        return new LegacyReviewPageResult(
                averageRating,
                content,
                totalPages,
                totalElements,
                last,
                first,
                pageNumber,
                pageSize,
                numberOfElements,
                empty);
    }
}
