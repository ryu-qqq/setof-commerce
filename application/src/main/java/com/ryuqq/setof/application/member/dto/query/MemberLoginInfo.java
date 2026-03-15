package com.ryuqq.setof.application.member.dto.query;

import com.ryuqq.setof.domain.member.aggregate.Member;

/**
 * 회원 로그인 정보.
 *
 * <p>회원 도메인 객체와 소셜 로그인 수단 정보를 함께 제공합니다.
 *
 * @param member 회원 도메인 객체
 * @param socialLoginType 소셜 로그인 타입
 * @param socialPkId 소셜 PK ID
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MemberLoginInfo(Member member, String socialLoginType, String socialPkId) {}
