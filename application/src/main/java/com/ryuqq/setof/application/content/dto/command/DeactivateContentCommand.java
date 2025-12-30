package com.ryuqq.setof.application.content.dto.command;

/**
 * Content 비활성화 Command
 *
 * @param contentId 콘텐츠 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeactivateContentCommand(Long contentId) {}
