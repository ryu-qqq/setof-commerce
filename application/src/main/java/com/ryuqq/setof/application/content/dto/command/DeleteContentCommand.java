package com.ryuqq.setof.application.content.dto.command;

/**
 * Content 삭제 Command
 *
 * @param contentId 콘텐츠 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteContentCommand(Long contentId) {}
