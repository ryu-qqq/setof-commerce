package com.ryuqq.setof.adapter.out.persistence.seller.condition;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerCsJpaEntity.sellerCsJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * SellerCsConditionBuilder - 셀러 CS 정보 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerCsConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? sellerCsJpaEntity.id.eq(id) : null;
    }

    /** 셀러 ID 일치 조건 */
    public BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? sellerCsJpaEntity.sellerId.eq(sellerId) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return sellerCsJpaEntity.deletedAt.isNull();
    }
}
