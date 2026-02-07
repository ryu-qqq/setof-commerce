package com.ryuqq.setof.storage.legacy.composite.web.faq.dto;

import com.ryuqq.setof.domain.legacy.faq.FaqType;

/**
 * LegacyWebFaqQueryDto - 레거시 Web FAQ 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param faqType FAQ 유형
 * @param title 제목
 * @param contents 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebFaqQueryDto(FaqType faqType, String title, String contents) {}
