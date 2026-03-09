package com.ryuqq.setof.application.member.dto.query;

import com.ryuqq.setof.domain.member.aggregate.Member;

/**
 * 회원 프로필 복합 결과 (등급 + 마일리지 포함).
 *
 * <p>레거시 fetchUser 응답에 필요한 users + user_grade + user_mileage JOIN 결과입니다.
 *
 * @param member 회원 도메인 객체
 * @param gradeName 등급 이름 (NORMAL_GRADE, GUEST 등)
 * @param currentMileage 현재 마일리지
 * @param socialLoginType 소셜 로그인 타입
 * @param socialPkId 소셜 PK ID
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MemberProfile(
        Member member,
        String gradeName,
        double currentMileage,
        String socialLoginType,
        String socialPkId) {}
