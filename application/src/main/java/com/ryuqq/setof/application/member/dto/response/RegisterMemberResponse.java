package com.ryuqq.setof.application.member.dto.response;

/**
 * Register Member Response
 *
 * <p>회원가입 응답 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record RegisterMemberResponse(String memberId, TokenPairResponse tokens) {}
