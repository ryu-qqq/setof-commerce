package com.ryuqq.setof.storage.legacy.composite.web.user.dto;

import java.time.LocalDateTime;

/**
 * 레거시 마이페이지 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param name 이름
 * @param phoneNumber 전화번호
 * @param email 이메일
 * @param socialLoginType 소셜 로그인 타입
 * @param insertDate 가입일
 * @param gradeName 등급명
 * @param currentMileage 현재 마일리지
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebMyPageQueryDto(
        String name,
        String phoneNumber,
        String email,
        String socialLoginType,
        LocalDateTime insertDate,
        String gradeName,
        double currentMileage) {}
