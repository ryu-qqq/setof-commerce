package com.ryuqq.setof.application.brand.dto.response;

import java.util.List;

/**
 * 브랜드 페이지 조회 결과.
 *
 * <p>APP-DTO-005: PageResult는 content + totalCount + page + size + hasNext 필수.
 *
 * @param content 브랜드 목록
 * @param totalCount 전체 개수
 * @param page 현재 페이지
 * @param size 페이지 크기
 * @param hasNext 다음 페이지 존재 여부
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandPageResult(
        List<BrandResult> content, long totalCount, int page, int size, boolean hasNext) {

    public static BrandPageResult of(
            List<BrandResult> content, long totalCount, int page, int size) {
        boolean hasNext = (long) (page + 1) * size < totalCount;
        return new BrandPageResult(content, totalCount, page, size, hasNext);
    }

    public static BrandPageResult empty() {
        return new BrandPageResult(List.of(), 0L, 0, 20, false);
    }
}
