package com.ryuqq.setof.storage.legacy.composite.web.gnb.dto;

/**
 * LegacyWebGnbQueryDto - 레거시 Web GNB 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param gnbId GNB ID
 * @param title 제목
 * @param linkUrl 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebGnbQueryDto(long gnbId, String title, String linkUrl) {}
