package com.ryuqq.setof.application.member.dto.command;

/**
 * 비밀번호 재설정 Command.
 *
 * @param phoneNumber 전화번호
 * @param newPassword 새 비밀번호 (평문)
 * @author ryu-qqq
 * @since 1.2.0
 */
public record ResetPasswordCommand(String phoneNumber, String newPassword) {

    public ResetPasswordCommand {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("phoneNumber must not be blank");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("newPassword must not be blank");
        }
    }
}
