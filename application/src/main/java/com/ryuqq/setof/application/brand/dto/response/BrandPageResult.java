package com.ryuqq.setof.application.brand.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

/**
 * 브랜드 페이지 조회 결과.
 *
 * <p>APP-DTO-005: PageResult는 content + PageMeta 구조.
 *
 * @param content 브랜드 목록
 * @param pageMeta 페이징 메타 정보
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandPageResult(List<BrandResult> content, PageMeta pageMeta) {

    public static BrandPageResult of(List<BrandResult> content, PageMeta pageMeta) {
        return new BrandPageResult(content, pageMeta);
    }

    public static BrandPageResult of(
            List<BrandResult> content, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new BrandPageResult(content, pageMeta);
    }

    public static BrandPageResult empty() {
        return new BrandPageResult(List.of(), PageMeta.empty());
    }
}
