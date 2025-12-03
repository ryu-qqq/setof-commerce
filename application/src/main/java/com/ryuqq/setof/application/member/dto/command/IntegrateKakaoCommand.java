package com.ryuqq.setof.application.member.dto.command;

import java.time.LocalDate;

/**
 * Integrate Kakao Command
 *
 * <p>카카오 계정 통합 요청 데이터
 *
 * <p>카카오 계정 연동 시 기존 LOCAL 회원의 프로필을 카카오에서 받은 정보로 업데이트합니다.
 *
 * @param memberId 기존 회원 ID (UUID 문자열)
 * @param kakaoId 카카오 소셜 ID
 * @param email 카카오에서 받은 이메일 (null 허용)
 * @param name 카카오에서 받은 이름 (null 허용)
 * @param dateOfBirth 카카오에서 받은 생년월일 (null 허용)
 * @param gender 카카오에서 받은 성별 (null 허용, M/F/N)
 * @author development-team
 * @since 1.0.0
 */
public record IntegrateKakaoCommand(
        String memberId,
        String kakaoId,
        String email,
        String name,
        LocalDate dateOfBirth,
        String gender) {

    /**
     * 기본 생성자 (프로필 정보 없이 카카오 연동만)
     *
     * @param memberId 기존 회원 ID
     * @param kakaoId 카카오 소셜 ID
     * @return IntegrateKakaoCommand
     */
    public static IntegrateKakaoCommand withoutProfile(String memberId, String kakaoId) {
        return new IntegrateKakaoCommand(memberId, kakaoId, null, null, null, null);
    }

    /**
     * 이메일 존재 여부 확인
     */
    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    /**
     * 이름 존재 여부 확인
     */
    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    /**
     * 생년월일 존재 여부 확인
     */
    public boolean hasDateOfBirth() {
        return dateOfBirth != null;
    }

    /**
     * 성별 존재 여부 확인
     */
    public boolean hasGender() {
        return gender != null && !gender.isBlank();
    }
}
