package com.ryuqq.setof.application.banner.dto.query;

import java.time.Instant;

/**
 * 배너 그룹 검색 파라미터 DTO.
 *
 * @param bannerType 배너 타입 문자열 필터 (nullable)
 * @param active 활성 여부 필터 (nullable)
 * @param displayPeriodStart 전시 기간 시작 (nullable)
 * @param displayPeriodEnd 전시 기간 종료 (nullable)
 * @param titleKeyword 제목 검색어 (nullable)
 * @param lastDomainId No-Offset 페이징용 마지막 ID (nullable)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BannerGroupSearchParams(
        String bannerType,
        Boolean active,
        Instant displayPeriodStart,
        Instant displayPeriodEnd,
        String titleKeyword,
        Long lastDomainId,
        int page,
        int size) {}
