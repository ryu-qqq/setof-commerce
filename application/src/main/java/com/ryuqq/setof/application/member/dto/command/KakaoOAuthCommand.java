package com.ryuqq.setof.application.member.dto.command;

import java.time.LocalDate;
import java.util.List;

import com.ryuqq.setof.application.member.dto.bundle.ConsentItem;

/**
 * Kakao OAuth Command
 *
 * <p>카카오 로그인 요청 데이터
 *
 * <p>integration 필드:
 *
 * <ul>
 *   <li>true: 기존 LOCAL 회원에 카카오 계정 연동 요청
 *   <li>false: 일반 카카오 로그인 (기존 회원이면 그대로 로그인)
 * </ul>
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
        List<ConsentItem> consents,
        boolean integration) {

    /** integration 없이 생성 (기본값: false) */
    public static KakaoOAuthCommand of(
            String kakaoId,
            String phoneNumber,
            String email,
            String name,
            LocalDate dateOfBirth,
            String gender,
            List<ConsentItem> consents) {
        return new KakaoOAuthCommand(
                kakaoId, phoneNumber, email, name, dateOfBirth, gender, consents, false);
    }

    /** integration 포함 생성 */
    public static KakaoOAuthCommand withIntegration(
            String kakaoId,
            String phoneNumber,
            String email,
            String name,
            LocalDate dateOfBirth,
            String gender,
            List<ConsentItem> consents,
            boolean integration) {
        return new KakaoOAuthCommand(
                kakaoId, phoneNumber, email, name, dateOfBirth, gender, consents, integration);
    }
}
