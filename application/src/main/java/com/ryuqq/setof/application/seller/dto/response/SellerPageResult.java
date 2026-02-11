package com.ryuqq.setof.application.seller.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

/**
 * 셀러 페이지 조회 결과.
 *
 * <p>APP-DTO-005: PageResult는 content + PageMeta 구조.
 *
 * @param content 셀러 목록
 * @param pageMeta 페이징 메타 정보
 */
public record SellerPageResult(List<SellerResult> content, PageMeta pageMeta) {

    public static SellerPageResult of(List<SellerResult> content, PageMeta pageMeta) {
        return new SellerPageResult(content, pageMeta);
    }

    public static SellerPageResult of(
            List<SellerResult> content, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new SellerPageResult(content, pageMeta);
    }

    public static SellerPageResult empty() {
        return new SellerPageResult(List.of(), PageMeta.empty());
    }
}
