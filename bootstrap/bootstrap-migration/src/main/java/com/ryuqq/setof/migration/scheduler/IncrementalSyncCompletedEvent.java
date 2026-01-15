package com.ryuqq.setof.migration.scheduler;

import org.springframework.context.ApplicationEvent;

/**
 * 증분 동기화 Job 완료 이벤트
 *
 * <p>Job이 완료(성공/실패)되면 발행되어 스케줄러의 동시 실행 락을 해제합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class IncrementalSyncCompletedEvent extends ApplicationEvent {

    private final String domainName;
    private final boolean success;

    public IncrementalSyncCompletedEvent(Object source, String domainName, boolean success) {
        super(source);
        this.domainName = domainName;
        this.success = success;
    }

    public String getDomainName() {
        return domainName;
    }

    public boolean isSuccess() {
        return success;
    }
}
