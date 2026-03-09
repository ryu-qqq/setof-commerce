package com.ryuqq.setof.application.member.dto.command;

/**
 * 카카오 소셜 로그인 Command.
 *
 * <p>카카오 OAuth2 인증 완료 후 SuccessHandler에서 생성됩니다.
 *
 * @param phoneNumber 전화번호 (정규화된 010 형식)
 * @param name 이름
 * @param email 이메일
 * @param socialPkId 카카오 고유 ID
 * @param gender 성별 (M, W)
 * @param dateOfBirth 생년월일 (yyyy-MM-dd)
 * @param integration 통합회원 처리 여부
 * @author ryu-qqq
 * @since 1.2.0
 */
public record KakaoLoginCommand(
        String phoneNumber,
        String name,
        String email,
        String socialPkId,
        String gender,
        String dateOfBirth,
        boolean integration) {

    public KakaoLoginCommand {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("phoneNumber must not be blank");
        }
        if (socialPkId == null || socialPkId.isBlank()) {
            throw new IllegalArgumentException("socialPkId must not be blank");
        }
    }
}
