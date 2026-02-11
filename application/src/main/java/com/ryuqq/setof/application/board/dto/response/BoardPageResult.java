package com.ryuqq.setof.application.board.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

/**
 * 공지사항 페이지 조회 결과.
 *
 * <p>APP-DTO-005: PageResult는 content + PageMeta 구조.
 *
 * @param content 공지사항 목록
 * @param pageMeta 페이징 메타 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BoardPageResult(List<BoardResult> content, PageMeta pageMeta) {

    public static BoardPageResult of(List<BoardResult> content, PageMeta pageMeta) {
        return new BoardPageResult(content, pageMeta);
    }

    public static BoardPageResult of(
            List<BoardResult> content, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new BoardPageResult(content, pageMeta);
    }

    public static BoardPageResult empty() {
        return new BoardPageResult(List.of(), PageMeta.empty());
    }
}
