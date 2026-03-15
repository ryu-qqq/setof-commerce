package com.ryuqq.setof.application.member.dto.query;

import com.ryuqq.setof.domain.order.vo.MyPageOrderCounts;
import java.time.Instant;

/**
 * 마이페이지 조회 결과.
 *
 * @param userId 회원 ID
 * @param phoneNumber 전화번호
 * @param name 이름
 * @param email 이메일
 * @param currentMileage 현재 마일리지
 * @param socialLoginType 소셜 로그인 타입
 * @param registrationDate 가입일
 * @param orderCounts 마이페이지 주문 상태별 건수
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MyPageResult(
        long userId,
        String phoneNumber,
        String name,
        String email,
        double currentMileage,
        String socialLoginType,
        Instant registrationDate,
        MyPageOrderCounts orderCounts) {}
