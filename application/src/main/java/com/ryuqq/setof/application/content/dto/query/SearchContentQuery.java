package com.ryuqq.setof.application.content.dto.query;

import java.time.Instant;

/**
 * Content 검색 Query
 *
 * @param title 제목 검색어 (nullable)
 * @param status 상태 필터 (nullable)
 * @param displayableAt 특정 시점에 노출 가능한 것만 (nullable)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record SearchContentQuery(
        String title, String status, Instant displayableAt, Integer page, Integer size) {

    /** 기본값 적용 생성자 */
    public SearchContentQuery {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
    }

    /**
     * 오프셋 계산
     *
     * @return 오프셋 값
     */
    public int offset() {
        return page * size;
    }
}
