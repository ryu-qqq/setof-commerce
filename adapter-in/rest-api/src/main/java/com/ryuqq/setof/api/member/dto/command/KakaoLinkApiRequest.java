package com.ryuqq.setof.api.member.dto.command;

import jakarta.validation.constraints.NotBlank;

/**
 * Kakao Link API Request
 *
 * <p>카카오 계정 연동 요청 DTO
 *
 * @param kakaoId 카카오 ID
 * @author development-team
 * @since 1.0.0
 */
public record KakaoLinkApiRequest(@NotBlank(message = "카카오 ID는 필수입니다.") String kakaoId) {}
