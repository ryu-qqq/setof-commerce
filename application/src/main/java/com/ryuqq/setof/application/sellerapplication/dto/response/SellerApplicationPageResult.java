package com.ryuqq.setof.application.sellerapplication.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

/**
 * 셀러 입점 신청 페이지 조회 결과.
 *
 * <p>APP-DTO-005: PageResult는 content + PageMeta 구조.
 *
 * @param content 입점 신청 목록
 * @param pageMeta 페이징 메타 정보
 */
public record SellerApplicationPageResult(
        List<SellerApplicationResult> content, PageMeta pageMeta) {

    public static SellerApplicationPageResult of(
            List<SellerApplicationResult> content, PageMeta pageMeta) {
        return new SellerApplicationPageResult(content, pageMeta);
    }

    public static SellerApplicationPageResult of(
            List<SellerApplicationResult> content, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new SellerApplicationPageResult(content, pageMeta);
    }

    public static SellerApplicationPageResult empty(int size) {
        return new SellerApplicationPageResult(List.of(), PageMeta.empty(size));
    }
}
