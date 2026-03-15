package com.ryuqq.setof.application.contentpage.dto;

import java.time.Instant;

/**
 * 콘텐츠 페이지 목록 검색 파라미터 DTO.
 *
 * @param active 활성 여부 필터 (nullable)
 * @param displayPeriodStart 전시 기간 시작 (nullable)
 * @param displayPeriodEnd 전시 기간 종료 (nullable)
 * @param createdAtStart 등록일 시작 (nullable)
 * @param createdAtEnd 등록일 종료 (nullable)
 * @param titleKeyword 제목 검색어 (nullable)
 * @param contentPageId ID 검색 (nullable)
 * @param lastDomainId No-Offset 페이징용 마지막 ID (nullable)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageSearchParams(
        Boolean active,
        Instant displayPeriodStart,
        Instant displayPeriodEnd,
        Instant createdAtStart,
        Instant createdAtEnd,
        String titleKeyword,
        Long contentPageId,
        Long lastDomainId,
        int page,
        int size) {}
