package com.ryuqq.setof.application.member.dto.query;

/**
 * 회원 프로필 조회 결과.
 *
 * @param userId 회원 ID
 * @param phoneNumber 전화번호
 * @param name 이름
 * @param email 이메일
 * @param currentMileage 현재 마일리지
 * @param socialLoginType 소셜 로그인 타입
 * @param socialPkId 소셜 PK ID
 * @param joinedDate 가입일
 * @param deleteYn 삭제 여부
 * @author ryu-qqq
 * @since 1.2.0
 */
public record UserResult(
        long userId,
        String phoneNumber,
        String name,
        String email,
        double currentMileage,
        String socialLoginType,
        String socialPkId,
        String joinedDate,
        String deleteYn) {}
