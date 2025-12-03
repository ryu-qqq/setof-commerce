package com.ryuqq.setof.application.member.event;

import com.ryuqq.setof.domain.core.member.aggregate.Member;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Member Event Dispatcher
 *
 * <p>도메인 이벤트 수집/발행/클리어 전담 컴포넌트
 *
 * <p>책임:
 *
 * <ul>
 *   <li>Member Aggregate에 등록된 도메인 이벤트 발행
 *   <li>발행 후 이벤트 클리어 (중복 발행 방지)
 * </ul>
 *
 * <p>사용 시점: 트랜잭션 커밋 후 호출 권장
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberEventDispatcher {

    private final ApplicationEventPublisher eventPublisher;

    public MemberEventDispatcher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 도메인 이벤트 발행 후 클리어
     *
     * <p>Member Aggregate에 등록된 모든 도메인 이벤트를 발행하고, 발행 완료 후 이벤트 목록을 클리어합니다.
     *
     * @param member 이벤트가 등록된 Member Aggregate
     */
    public void publishAndClear(Member member) {
        member.getDomainEvents().forEach(eventPublisher::publishEvent);
        member.clearDomainEvents();
    }

    /**
     * 도메인 이벤트 발행만 수행 (클리어하지 않음)
     *
     * <p>특수한 경우에만 사용. 일반적으로는 publishAndClear 사용 권장.
     *
     * @param member 이벤트가 등록된 Member Aggregate
     */
    public void publishOnly(Member member) {
        member.getDomainEvents().forEach(eventPublisher::publishEvent);
    }
}
