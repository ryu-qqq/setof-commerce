package com.ryuqq.setof.domain.contentpage.query;

/**
 * ContentPageSearchCriteria - 콘텐츠 페이지 검색 조건.
 *
 * @param contentPageId 콘텐츠 페이지 ID (nullable)
 * @param bypass 삭제된 항목 포함 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageSearchCriteria(Long contentPageId, boolean bypass) {}
