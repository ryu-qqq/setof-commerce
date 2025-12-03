package com.ryuqq.setof.application.member.dto.command;

/**
 * Logout Member Command
 *
 * <p>로그아웃 요청 데이터를 담는 순수한 불변 객체
 *
 * @author development-team
 * @since 1.0.0
 */
public record LogoutMemberCommand(String memberId) {}
