package com.ryuqq.setof.application.member.dto.command;

/**
 * 비밀번호 변경 Context.
 *
 * @param userId 레거시 user_id
 * @param encodedPassword BCrypt 해시된 새 비밀번호
 * @author ryu-qqq
 * @since 1.2.0
 */
public record UpdatePasswordContext(long userId, String encodedPassword) {}
