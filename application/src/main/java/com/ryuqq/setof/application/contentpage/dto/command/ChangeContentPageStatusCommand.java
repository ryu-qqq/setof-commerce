package com.ryuqq.setof.application.contentpage.dto.command;

/**
 * ChangeContentPageStatusCommand - 콘텐츠 페이지 노출 상태 변경 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다.
 *
 * @param id 콘텐츠 페이지 ID
 * @param active 변경할 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ChangeContentPageStatusCommand(long id, boolean active) {}
