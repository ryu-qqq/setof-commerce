package com.ryuqq.setof.application.member.dto.response;

/**
 * Local Login Response
 *
 * <p>핸드폰 번호 + 비밀번호 로그인 결과 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record LocalLoginResponse(String memberId, TokenPairResponse tokens) {}
