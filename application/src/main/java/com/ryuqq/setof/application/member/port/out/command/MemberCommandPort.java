package com.ryuqq.setof.application.member.port.out.command;

import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.domain.member.aggregate.Member;

/**
 * 회원 명령 Port.
 *
 * <p>레거시 DB에서 회원 persist 작업을 수행하는 아웃바운드 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface MemberCommandPort {

    /**
     * 신규 회원 저장.
     *
     * @param member 회원 도메인 객체
     * @param registrationInfo 가입 부가 정보 (비밀번호, 동의 등)
     * @return 생성된 사용자 ID
     */
    Long persist(Member member, MemberRegistrationInfo registrationInfo);

    /**
     * 회원 상태 변경 저장 - 탈퇴 (더티 체킹).
     *
     * @param userId 레거시 user_id
     * @param withdrawalReason 탈퇴 사유
     */
    void persistWithdrawal(long userId, String withdrawalReason);

    /**
     * 비밀번호 변경 저장 (더티 체킹).
     *
     * @param userId 레거시 user_id
     * @param encodedPassword BCrypt 해시된 새 비밀번호
     */
    void persistPasswordChange(long userId, String encodedPassword);

    /**
     * 소셜 로그인 통합 저장.
     *
     * <p>기존 전화번호 회원에 소셜 로그인 정보(socialLoginType, socialPkId, gender, dateOfBirth)를 업데이트합니다.
     *
     * @param userId 레거시 user_id
     * @param socialLoginType 소셜 로그인 타입 (KAKAO 등)
     * @param socialPkId 소셜 PK ID
     * @param gender 성별
     * @param dateOfBirth 생년월일
     */
    void persistSocialIntegration(
            long userId,
            String socialLoginType,
            String socialPkId,
            String gender,
            String dateOfBirth);
}
