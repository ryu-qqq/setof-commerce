package com.ryuqq.setof.application.qna.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

/**
 * QnaPageResult - 상품 Q&A 목록 Offset 기반 페이징 결과 DTO.
 *
 * <p>GET /api/v1/qna/product/{productGroupId} 응답에 사용됩니다.
 *
 * @param content Q&A 항목 목록
 * @param pageMeta 페이지 메타 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaPageResult(List<QnaDetailResult> content, PageMeta pageMeta) {

    public static QnaPageResult of(
            List<QnaDetailResult> content, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new QnaPageResult(content, pageMeta);
    }

    public static QnaPageResult empty(int page, int size) {
        PageMeta pageMeta = PageMeta.of(page, size, 0L);
        return new QnaPageResult(List.of(), pageMeta);
    }

    public boolean hasNext() {
        return pageMeta.hasNext();
    }
}
