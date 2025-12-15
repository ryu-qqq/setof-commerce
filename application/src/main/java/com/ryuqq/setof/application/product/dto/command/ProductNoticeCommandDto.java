package com.ryuqq.setof.application.product.dto.command;

import com.ryuqq.setof.application.productnotice.dto.command.NoticeItemDto;
import java.util.List;

/**
 * 상품고시 Command DTO
 *
 * <p>상품그룹 고시정보 등록/수정 데이터
 *
 * @param id 고시 ID (수정 시 사용, null이면 신규)
 * @param templateId 템플릿 ID
 * @param items 고시 항목 목록
 * @author development-team
 * @since 1.0.0
 */
public record ProductNoticeCommandDto(Long id, Long templateId, List<NoticeItemDto> items) {}
