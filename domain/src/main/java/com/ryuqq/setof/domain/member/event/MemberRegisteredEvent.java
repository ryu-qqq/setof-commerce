package com.ryuqq.setof.domain.member.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.time.Instant;

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
 * <p>Instant 사용 이유:
 *
 * <ul>
 *   <li>타임존 독립적인 절대 시간 표현
 *   <li>이벤트 발생 시점의 정확한 기록
 *   <li>분산 시스템에서 일관된 시간 비교 가능
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
        Instant occurredAt)
        implements DomainEvent {

    /**
     * 이벤트 생성 팩토리 메서드
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param phoneNumber 핸드폰 번호
     * @param email 이메일 (nullable)
     * @param name 회원명
     * @param authProvider 인증 제공자 (LOCAL, KAKAO)
     * @param registeredAt 가입 시각 (UTC 기준)
     * @return MemberRegisteredEvent 인스턴스
     */
    public static MemberRegisteredEvent of(
            String memberId,
            String phoneNumber,
            String email,
            String name,
            String authProvider,
            Instant registeredAt) {
        return new MemberRegisteredEvent(
                memberId, phoneNumber, email, name, authProvider, registeredAt);
    }

    /**
     * Member Aggregate에서 이벤트 생성 팩토리 메서드
     *
     * <p>Aggregate Root에서 직접 이벤트를 생성할 때 사용합니다.
     *
     * @param member 회원 Aggregate
     * @param occurredAt 이벤트 발생 시각 (UTC 기준)
     * @return MemberRegisteredEvent 인스턴스
     */
    public static MemberRegisteredEvent from(Member member, Instant occurredAt) {
        return new MemberRegisteredEvent(
                member.getIdValue(),
                member.getPhoneNumberValue(),
                member.getEmailValue(),
                member.getNameValue(),
                member.getProvider().name(),
                occurredAt);
    }
}
