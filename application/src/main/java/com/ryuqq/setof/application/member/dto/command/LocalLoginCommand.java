package com.ryuqq.setof.application.member.dto.command;

/**
 * Local Login Command
 *
 * <p>핸드폰 번호 + 비밀번호 로그인 요청 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record LocalLoginCommand(String phoneNumber, String rawPassword) {
    // Immutable command object - no additional behavior
}
