package com.ryuqq.setof.domain.cms.query.criteria;

import com.ryuqq.setof.domain.cms.vo.ContentStatus;
import java.time.Instant;

/**
 * Content 검색 조건 Criteria
 *
 * <p>Content 목록 조회 시 사용하는 검색 조건
 *
 * @param title 제목 검색어 (nullable - contains 검색)
 * @param status 상태 필터 (nullable)
 * @param displayableAt 특정 시점 노출 가능 필터 (nullable)
 * @param offset 오프셋
 * @param limit 조회 개수
 * @author development-team
 * @since 1.0.0
 */
public record ContentSearchCriteria(
        String title, ContentStatus status, Instant displayableAt, int offset, int limit) {

    /**
     * 검색 조건 생성
     *
     * @param title 제목 검색어
     * @param status 상태
     * @param displayableAt 노출 시점
     * @param offset 오프셋
     * @param limit 조회 개수
     * @return ContentSearchCriteria 인스턴스
     */
    public static ContentSearchCriteria of(
            String title, ContentStatus status, Instant displayableAt, int offset, int limit) {
        return new ContentSearchCriteria(title, status, displayableAt, offset, limit);
    }

    /**
     * 제목 검색 여부
     *
     * @return 제목 검색어가 있으면 true
     */
    public boolean hasTitle() {
        return title != null && !title.isBlank();
    }

    /**
     * 상태 필터 여부
     *
     * @return 상태 필터가 있으면 true
     */
    public boolean hasStatus() {
        return status != null;
    }

    /**
     * 노출 시점 필터 여부
     *
     * @return 노출 시점 필터가 있으면 true
     */
    public boolean hasDisplayableAt() {
        return displayableAt != null;
    }
}
