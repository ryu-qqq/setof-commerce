package com.ryuqq.setof.adapter.out.persistence.seller.condition;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerBusinessInfoJpaEntity.sellerBusinessInfoJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * SellerBusinessInfoConditionBuilder - 셀러 사업자 정보 QueryDSL 조건 빌더.
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
public class SellerBusinessInfoConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? sellerBusinessInfoJpaEntity.id.eq(id) : null;
    }

    /** 셀러 ID 일치 조건 */
    public BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? sellerBusinessInfoJpaEntity.sellerId.eq(sellerId) : null;
    }

    /** 사업자등록번호 일치 조건 */
    public BooleanExpression registrationNumberEq(String registrationNumber) {
        return registrationNumber != null && !registrationNumber.isBlank()
                ? sellerBusinessInfoJpaEntity.registrationNumber.eq(registrationNumber)
                : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return sellerBusinessInfoJpaEntity.deletedAt.isNull();
    }
}
