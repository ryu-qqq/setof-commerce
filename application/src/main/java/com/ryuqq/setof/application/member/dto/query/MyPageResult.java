package com.ryuqq.setof.application.member.dto.query;

import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import java.time.Instant;
import java.util.List;

/**
 * 마이페이지 조회 결과.
 *
 * <p>회원 프로필 정보와 주문 상태별 건수를 포함합니다.
 *
 * @param userId 레거시 user_id
 * @param phoneNumber 전화번호
 * @param name 이름
 * @param email 이메일
 * @param gradeName 등급 이름
 * @param currentMileage 현재 마일리지
 * @param socialLoginType 소셜 로그인 타입
 * @param orderCounts 주문 상태별 건수
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MyPageResult(
        long userId,
        String phoneNumber,
        String name,
        String email,
        String gradeName,
        double currentMileage,
        String socialLoginType,
        Instant registrationDate,
        List<OrderStatusCountResult> orderCounts) {}
