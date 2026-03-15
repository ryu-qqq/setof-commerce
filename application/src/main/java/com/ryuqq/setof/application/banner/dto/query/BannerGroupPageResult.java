package com.ryuqq.setof.application.banner.dto.query;

import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import java.util.List;

/**
 * 배너 그룹 페이지 조회 결과 DTO.
 *
 * @param items 현재 페이지의 배너 그룹 목록
 * @param totalCount 전체 항목 수
 * @param page 현재 페이지 번호
 * @param size 페이지 크기
 * @param lastDomainId No-Offset 페이징용 마지막 ID (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BannerGroupPageResult(
        List<BannerGroup> items, long totalCount, int page, int size, Long lastDomainId) {

    public static BannerGroupPageResult of(
            List<BannerGroup> items, long totalCount, int page, int size, Long lastDomainId) {
        return new BannerGroupPageResult(items, totalCount, page, size, lastDomainId);
    }
}
