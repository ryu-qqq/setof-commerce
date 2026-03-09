package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.discount.id.DiscountOutboxId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import com.ryuqq.setof.domain.discount.vo.OutboxTargetKey;
import java.time.Instant;

/**
 * DiscountOutbox - 할인 가격 재계산 아웃박스 Aggregate Root.
 *
 * <p>할인 정책 변경 시 영향받는 타겟(브랜드, 셀러, 카테고리, 상품)별로 아웃박스를 생성하고, 스케줄러가 SQS로 발행하면 Consumer가 가격을 재계산하여 legacy
 * product_group 테이블을 갱신합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>상태 전이는 PENDING → PUBLISHED → COMPLETED/FAILED 순서만 허용
 *   <li>동일 타겟에 PENDING 최대 1건, PUBLISHED 최대 1건
 *   <li>retryCount가 maxRetry를 초과하면 FAILED로 전환
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class DiscountOutbox {

    private static final int MAX_RETRY = 3;

    private final DiscountOutboxId id;
    private final OutboxTargetKey targetKey;
    private OutboxStatus status;
    private int retryCount;
    private String payload;
    private String failReason;
    private final Instant createdAt;
    private Instant updatedAt;

    private DiscountOutbox(
            DiscountOutboxId id,
            OutboxTargetKey targetKey,
            OutboxStatus status,
            int retryCount,
            String payload,
            String failReason,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.targetKey = targetKey;
        this.status = status;
        this.retryCount = retryCount;
        this.payload = payload;
        this.failReason = failReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 아웃박스 항목 생성.
     *
     * @param targetType 할인 대상 유형
     * @param targetId 대상 ID
     * @param now 생성 시각
     * @return 새 DiscountOutbox 인스턴스 (PENDING 상태)
     */
    public static DiscountOutbox forNew(DiscountTargetType targetType, long targetId, Instant now) {
        return new DiscountOutbox(
                DiscountOutboxId.forNew(),
                OutboxTargetKey.of(targetType, targetId),
                OutboxStatus.PENDING,
                0,
                null,
                null,
                now,
                now);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @return 복원된 DiscountOutbox 인스턴스
     */
    public static DiscountOutbox reconstitute(
            DiscountOutboxId id,
            OutboxTargetKey targetKey,
            OutboxStatus status,
            int retryCount,
            String payload,
            String failReason,
            Instant createdAt,
            Instant updatedAt) {
        return new DiscountOutbox(
                id, targetKey, status, retryCount, payload, failReason, createdAt, updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * SQS 발행 완료 처리.
     *
     * @param payload SQS 메시지 페이로드 (JSON)
     * @param now 발행 시각
     * @throws IllegalStateException PENDING 상태가 아닐 때
     */
    public void markPublished(String payload, Instant now) {
        validateTransition(OutboxStatus.PENDING, OutboxStatus.PUBLISHED);
        this.status = OutboxStatus.PUBLISHED;
        this.payload = payload;
        this.updatedAt = now;
    }

    /**
     * 처리 완료.
     *
     * @param now 완료 시각
     * @throws IllegalStateException PUBLISHED 상태가 아닐 때
     */
    public void markCompleted(Instant now) {
        validateTransition(OutboxStatus.PUBLISHED, OutboxStatus.COMPLETED);
        this.status = OutboxStatus.COMPLETED;
        this.updatedAt = now;
    }

    /**
     * 처리 실패 + 재시도 판단.
     *
     * <p>retryCount가 MAX_RETRY 미만이면 PENDING으로 복구, 이상이면 FAILED.
     *
     * @param reason 실패 사유
     * @param now 실패 시각
     */
    public void markFailed(String reason, Instant now) {
        this.retryCount++;
        this.failReason = reason;
        this.updatedAt = now;

        if (this.retryCount >= MAX_RETRY) {
            this.status = OutboxStatus.FAILED;
        } else {
            this.status = OutboxStatus.PENDING;
        }
    }

    /**
     * Stuck 복구 (PUBLISHED 상태에서 오래 머문 경우).
     *
     * @param now 복구 시각
     * @throws IllegalStateException PUBLISHED 상태가 아닐 때
     */
    public void recoverStuck(Instant now) {
        if (this.status != OutboxStatus.PUBLISHED) {
            throw new IllegalStateException("PUBLISHED 상태에서만 stuck 복구 가능합니다. 현재 상태: " + status);
        }
        this.retryCount++;
        this.updatedAt = now;

        if (this.retryCount >= MAX_RETRY) {
            this.status = OutboxStatus.FAILED;
            this.failReason = "Stuck recovery exceeded max retry";
        } else {
            this.status = OutboxStatus.PENDING;
        }
    }

    // ========== Validation ==========

    private void validateTransition(OutboxStatus expected, OutboxStatus target) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    target + " 전이는 " + expected + " 상태에서만 가능합니다. 현재 상태: " + status);
        }
    }

    // ========== Condition Checks ==========

    /** PENDING 상태인지 확인 */
    public boolean isPending() {
        return status == OutboxStatus.PENDING;
    }

    /** PUBLISHED 상태인지 확인 */
    public boolean isPublished() {
        return status == OutboxStatus.PUBLISHED;
    }

    /** 최대 재시도 횟수 도달 여부 */
    public boolean isMaxRetryExceeded() {
        return retryCount >= MAX_RETRY;
    }

    // ========== Accessor Methods ==========

    public DiscountOutboxId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public OutboxTargetKey targetKey() {
        return targetKey;
    }

    public DiscountTargetType targetType() {
        return targetKey.targetType();
    }

    public long targetId() {
        return targetKey.targetId();
    }

    public String targetKeyValue() {
        return targetKey.toKey();
    }

    public OutboxStatus status() {
        return status;
    }

    public int retryCount() {
        return retryCount;
    }

    public String payload() {
        return payload;
    }

    public String failReason() {
        return failReason;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
