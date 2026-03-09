package com.ryuqq.setof.storage.legacy.composite.seller.condition;

import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerBusinessInfoEntity.legacySellerBusinessInfoEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSellerCompositeConditionBuilder - 레거시 Web 판매자 Composite QueryDSL 조건 빌더.
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
public class LegacyWebSellerCompositeConditionBuilder {

    /**
     * 판매자 ID 일치 조건.
     *
     * @param sellerId 판매자 ID
     * @return BooleanExpression
     */
    public BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? legacySellerEntity.id.eq(sellerId) : null;
    }

    /**
     * 사업자등록번호 일치 조건.
     *
     * @param registrationNumber 사업자등록번호
     * @return BooleanExpression
     */
    public BooleanExpression registrationNumberEq(String registrationNumber) {
        return registrationNumber != null
                ? legacySellerBusinessInfoEntity.registrationNumber.eq(registrationNumber)
                : null;
    }
}
