package com.ryuqq.setof.application.member.dto.command;

/**
 * 회원 탈퇴 Command.
 *
 * @param userId 레거시 user_id
 * @param withdrawalReason 탈퇴 사유
 * @author ryu-qqq
 * @since 1.2.0
 */
public record WithdrawalCommand(long userId, String withdrawalReason) {}
