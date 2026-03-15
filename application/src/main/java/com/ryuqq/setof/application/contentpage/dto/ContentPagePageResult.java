package com.ryuqq.setof.application.contentpage.dto;

import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import java.util.List;

/**
 * 콘텐츠 페이지 페이지 조회 결과 DTO.
 *
 * @param items 현재 페이지의 콘텐츠 페이지 목록
 * @param totalCount 전체 항목 수
 * @param page 현재 페이지 번호
 * @param size 페이지 크기
 * @param lastDomainId No-Offset 페이징용 마지막 ID (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPagePageResult(
        List<ContentPage> items, long totalCount, int page, int size, Long lastDomainId) {

    public static ContentPagePageResult of(
            List<ContentPage> items, long totalCount, int page, int size, Long lastDomainId) {
        return new ContentPagePageResult(items, totalCount, page, size, lastDomainId);
    }
}
