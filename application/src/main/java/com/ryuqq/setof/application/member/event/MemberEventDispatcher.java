package com.ryuqq.setof.application.member.event;

import com.ryuqq.setof.domain.member.aggregate.Member;
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
     * 도메인 이벤트 발행 (pullDomainEvents 패턴)
     *
     * <p>Member Aggregate에 등록된 모든 도메인 이벤트를 발행합니다. pullDomainEvents()는 이벤트를 반환하면서 동시에 내부 목록을 클리어하므로
     * 중복 발행이 방지됩니다.
     *
     * @param member 이벤트가 등록된 Member Aggregate
     */
    public void publish(Member member) {
        member.pullDomainEvents().forEach(eventPublisher::publishEvent);
    }
}
