package com.ryuqq.setof.application.navigation.dto.query;

import java.time.Instant;

/**
 * 네비게이션 메뉴 검색 파라미터 DTO.
 *
 * @param displayPeriodStart 전시 기간 시작 (nullable)
 * @param displayPeriodEnd 전시 기간 종료 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NavigationMenuSearchParams(Instant displayPeriodStart, Instant displayPeriodEnd) {}
