package com.ryuqq.setof.domain.core.member.event;

import com.ryuqq.setof.domain.core.common.event.DomainEvent;
import java.time.LocalDateTime;

/**
 * 회원 가입 완료 도메인 이벤트
 *
 * <p>회원 가입이 완료되면 발행되는 이벤트. 순수 POJO로 Spring 의존성 없음.
 *
 * <p>이벤트 소비자 예시:
 *
 * <ul>
 *   <li>웰컴 이메일 발송
 *   <li>가입 축하 포인트 지급
 *   <li>통계 집계
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public record MemberRegisteredEvent(
        String memberId,
        String phoneNumber,
        String email,
        String name,
        String authProvider,
        LocalDateTime registeredAt)
        implements DomainEvent {

    /**
     * 이벤트 생성 팩토리 메서드
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param phoneNumber 핸드폰 번호
     * @param email 이메일 (nullable)
     * @param name 회원명
     * @param authProvider 인증 제공자 (LOCAL, KAKAO)
     * @param registeredAt 가입 시각
     * @return MemberRegisteredEvent 인스턴스
     */
    public static MemberRegisteredEvent of(
            String memberId,
            String phoneNumber,
            String email,
            String name,
            String authProvider,
            LocalDateTime registeredAt) {
        return new MemberRegisteredEvent(
                memberId, phoneNumber, email, name, authProvider, registeredAt);
    }
}
