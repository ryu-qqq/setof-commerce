package com.ryuqq.setof.adapter.out.persistence.refundaccount.condition;

import static com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.QRefundAccountJpaEntity.refundAccountJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * RefundAccountConditionBuilder - 환불 계좌 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * <p>NOTE: RefundAccountJpaEntity는 memberId(Long) 컬럼을 보유합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountConditionBuilder {

    /** 회원 ID 일치 조건 */
    public BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? refundAccountJpaEntity.memberId.eq(memberId) : null;
    }

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? refundAccountJpaEntity.id.eq(id) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return refundAccountJpaEntity.deletedAt.isNull();
    }
}
