package com.ryuqq.setof.domain.notification.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.notification.vo.NotificationChannel;
import com.ryuqq.setof.domain.notification.vo.NotificationEventType;
import com.ryuqq.setof.domain.notification.vo.NotificationStatus;

/**
 * NotificationOutbox 검색 조건 Criteria.
 *
 * <p>알림 아웃박스 모니터링 및 조회 시 사용합니다.
 *
 * @param channel 발송 채널 필터 (nullable)
 * @param eventType 이벤트 유형 필터 (nullable)
 * @param status 상태 필터 (nullable)
 * @param queryContext 정렬 및 페이징 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NotificationOutboxSearchCriteria(
        NotificationChannel channel,
        NotificationEventType eventType,
        NotificationStatus status,
        QueryContext<NotificationOutboxSortKey> queryContext) {

    public static NotificationOutboxSearchCriteria of(
            NotificationChannel channel,
            NotificationEventType eventType,
            NotificationStatus status,
            QueryContext<NotificationOutboxSortKey> queryContext) {
        return new NotificationOutboxSearchCriteria(channel, eventType, status, queryContext);
    }

    /** 기본 검색 조건 (전체, 생성일시 내림차순) */
    public static NotificationOutboxSearchCriteria defaultCriteria() {
        return new NotificationOutboxSearchCriteria(
                null, null, null, QueryContext.defaultOf(NotificationOutboxSortKey.defaultKey()));
    }

    /** 특정 상태의 아웃박스 조회 */
    public static NotificationOutboxSearchCriteria forStatus(NotificationStatus status) {
        return new NotificationOutboxSearchCriteria(
                null, null, status, QueryContext.defaultOf(NotificationOutboxSortKey.defaultKey()));
    }

    public int size() {
        return queryContext.size();
    }

    public long offset() {
        return queryContext.offset();
    }

    public int page() {
        return queryContext.page();
    }
}
