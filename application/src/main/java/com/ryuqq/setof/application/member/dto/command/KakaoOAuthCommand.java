package com.ryuqq.setof.application.member.dto.command;

import java.time.LocalDate;
import java.util.List;

/**
 * Kakao OAuth Command
 *
 * <p>카카오 로그인 요청 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record KakaoOAuthCommand(
        String kakaoId,
        String phoneNumber,
        String email,
        String name,
        LocalDate dateOfBirth,
        String gender,
        List<ConsentItem> consents) {
}
