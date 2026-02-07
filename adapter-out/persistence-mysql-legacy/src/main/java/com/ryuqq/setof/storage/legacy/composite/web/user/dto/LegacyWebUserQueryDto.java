package com.ryuqq.setof.storage.legacy.composite.web.user.dto;

import java.time.LocalDateTime;

/**
 * 레거시 사용자 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param userId 사용자 ID
 * @param name 이름
 * @param phoneNumber 전화번호
 * @param socialLoginType 소셜 로그인 타입
 * @param gradeName 등급명
 * @param currentMileage 현재 마일리지
 * @param insertDate 가입일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebUserQueryDto(
        long userId,
        String name,
        String phoneNumber,
        String socialLoginType,
        String gradeName,
        double currentMileage,
        LocalDateTime insertDate) {}
