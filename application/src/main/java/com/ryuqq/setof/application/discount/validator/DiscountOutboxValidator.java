package com.ryuqq.setof.application.discount.validator;

import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import org.springframework.stereotype.Component;

/**
 * DiscountOutboxValidator - 할인 아웃박스 중복 검증 Validator.
 *
 * <p>동일 타겟에 대해 PENDING 또는 PUBLISHED 상태의 아웃박스가 이미 존재하는지 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountOutboxValidator {

    private final DiscountOutboxReadManager outboxReadManager;

    public DiscountOutboxValidator(DiscountOutboxReadManager outboxReadManager) {
        this.outboxReadManager = outboxReadManager;
    }

    /**
     * 해당 타겟에 대해 아웃박스를 새로 생성해도 되는지 검증합니다.
     *
     * <p>PENDING 또는 PUBLISHED 상태의 아웃박스가 이미 존재하면 false를 반환합니다.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 생성 가능하면 true, 이미 존재하면 false
     */
    public boolean canCreateOutbox(DiscountTargetType targetType, long targetId) {
        boolean pendingExists = outboxReadManager.existsPendingForTarget(targetType, targetId);
        boolean publishedExists = outboxReadManager.existsPublishedForTarget(targetType, targetId);
        return !pendingExists && !publishedExists;
    }
}
