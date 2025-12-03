package com.ryuqq.setof.application.member.dto.command;

/**
 * Reset Password Command
 *
 * <p>비밀번호 재설정 요청 데이터를 담는 순수한 불변 객체
 *
 * @author development-team
 * @since 1.0.0
 */
public record ResetPasswordCommand(String phoneNumber, String newRawPassword) {}
