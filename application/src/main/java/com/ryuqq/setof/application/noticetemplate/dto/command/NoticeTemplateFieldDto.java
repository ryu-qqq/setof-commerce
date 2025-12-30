package com.ryuqq.setof.application.noticetemplate.dto.command;

/**
 * 상품고시 템플릿 필드 DTO
 *
 * @param key 필드 키
 * @param description 필드 설명
 * @param required 필수 여부
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record NoticeTemplateFieldDto(
        String key, String description, boolean required, int displayOrder) {}
