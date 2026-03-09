package com.ryuqq.setof.application.discount.port.out.query;

import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * 할인 아웃박스 조회 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountOutboxQueryPort {

    /**
     * ID로 아웃박스 조회.
     *
     * @param id 아웃박스 ID
     * @return Optional 아웃박스
     */
    Optional<DiscountOutbox> findById(long id);

    /**
     * 지정 상태의 아웃박스 항목을 배치 크기만큼 조회.
     *
     * @param status 대상 상태
     * @param batchSize 최대 조회 건수
     * @return 아웃박스 목록
     */
    List<DiscountOutbox> findByStatus(OutboxStatus status, int batchSize);

    /**
     * PUBLISHED 상태에서 일정 시간 이상 머문 stuck 항목 조회.
     *
     * @param timeoutBefore 이 시각 이전에 업데이트된 항목
     * @param batchSize 최대 조회 건수
     * @return stuck 아웃박스 목록
     */
    List<DiscountOutbox> findStuckPublished(Instant timeoutBefore, int batchSize);

    /**
     * 동일 타겟에 지정 상태인 항목 존재 여부.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @param status 확인할 상태
     * @return 존재 여부
     */
    boolean existsByTargetAndStatus(
            DiscountTargetType targetType, long targetId, OutboxStatus status);
}
