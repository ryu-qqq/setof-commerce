package com.ryuqq.setof.storage.legacy.refundaccount.condition;

import static com.ryuqq.setof.storage.legacy.refundaccount.entity.QLegacyRefundAccountEntity.legacyRefundAccountEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.common.Yn;
import org.springframework.stereotype.Component;

/**
 * LegacyRefundAccountConditionBuilder - 레거시 환불 계좌 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyRefundAccountConditionBuilder {

    /**
     * 사용자 ID 일치 조건.
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyRefundAccountEntity.userId.eq(userId) : null;
    }

    /**
     * 환불 계좌 ID 일치 조건.
     *
     * @param refundAccountId 환불 계좌 ID
     * @return BooleanExpression
     */
    public BooleanExpression idEq(Long refundAccountId) {
        return refundAccountId != null ? legacyRefundAccountEntity.id.eq(refundAccountId) : null;
    }

    /**
     * 소프트 삭제 필터 - deleteYn = 'N' 조건.
     *
     * <p>fetchRefundAccount (조회용) 에서만 사용. fetchRefundAccountEntity (엔티티 조회용) 에서는 사용하지 않음.
     *
     * @return BooleanExpression
     */
    public BooleanExpression notDeleted() {
        return legacyRefundAccountEntity.deleteYn.eq(Yn.N);
    }
}
