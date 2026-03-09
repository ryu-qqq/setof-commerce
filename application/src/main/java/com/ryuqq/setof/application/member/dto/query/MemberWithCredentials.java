package com.ryuqq.setof.application.member.dto.query;

import com.ryuqq.setof.domain.member.aggregate.Member;

/**
 * 회원 + 인증 정보 복합 결과.
 *
 * <p>로그인 시 비밀번호 검증과 소셜 로그인 타입 확인에 사용됩니다. 레거시 users 테이블에 인증 정보가 함께 저장되어 있기 때문에 한 번의 조회로 반환합니다.
 *
 * @param member 회원 도메인 객체
 * @param passwordHash BCrypt 해시된 비밀번호
 * @param socialLoginType 소셜 로그인 타입 (EMAIL, KAKAO 등)
 * @param socialPkId 소셜 PK ID
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MemberWithCredentials(
        Member member, String passwordHash, String socialLoginType, String socialPkId) {}
