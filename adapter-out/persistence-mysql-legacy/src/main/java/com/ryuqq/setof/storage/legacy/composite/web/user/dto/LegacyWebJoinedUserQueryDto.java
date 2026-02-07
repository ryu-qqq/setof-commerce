package com.ryuqq.setof.storage.legacy.composite.web.user.dto;

import java.time.LocalDateTime;

/**
 * 레거시 가입 사용자 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param name 이름
 * @param userId 사용자 ID
 * @param socialLoginType 소셜 로그인 타입
 * @param phoneNumber 전화번호
 * @param socialPkId 소셜 PK ID (nullable, coalesce 처리)
 * @param currentMileage 현재 마일리지
 * @param insertDate 가입일
 * @param deleteYn 삭제 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebJoinedUserQueryDto(
        String name,
        long userId,
        String socialLoginType,
        String phoneNumber,
        String socialPkId,
        double currentMileage,
        LocalDateTime insertDate,
        String deleteYn) {}
