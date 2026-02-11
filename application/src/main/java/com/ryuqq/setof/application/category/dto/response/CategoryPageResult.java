package com.ryuqq.setof.application.category.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

/**
 * 카테고리 페이지 조회 결과.
 *
 * <p>APP-DTO-005: PageResult는 content + PageMeta 구조.
 *
 * @param content 카테고리 목록
 * @param pageMeta 페이징 메타 정보
 * @author ryu-qqq
 * @since 1.0.0
 */
public record CategoryPageResult(List<CategoryResult> content, PageMeta pageMeta) {

    public static CategoryPageResult of(List<CategoryResult> content, PageMeta pageMeta) {
        return new CategoryPageResult(content, pageMeta);
    }

    public static CategoryPageResult of(
            List<CategoryResult> content, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new CategoryPageResult(content, pageMeta);
    }

    public static CategoryPageResult empty() {
        return new CategoryPageResult(List.of(), PageMeta.empty());
    }
}
