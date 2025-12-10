package com.ryuqq.setof.application.member.dto.response;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;

/**
 * Register Member Response
 *
 * <p>회원가입 응답 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record RegisterMemberResponse(String memberId, TokenPairResponse tokens) {
    // Immutable value object - no additional behavior
}
