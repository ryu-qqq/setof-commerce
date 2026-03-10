package com.ryuqq.setof.adapter.out.persistence.product.condition;

import static com.ryuqq.setof.adapter.out.persistence.product.entity.QProductJpaEntity.productJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductConditionBuilder - 상품 QueryDSL 조건 빌더.
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
public class ProductConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? productJpaEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? productJpaEntity.id.in(ids) : null;
    }

    /** productGroupId 일치 조건 */
    public BooleanExpression productGroupIdEq(Long productGroupId) {
        return productGroupId != null ? productJpaEntity.productGroupId.eq(productGroupId) : null;
    }

    /** productGroupId 목록 포함 조건 */
    public BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        return productGroupIds != null && !productGroupIds.isEmpty()
                ? productJpaEntity.productGroupId.in(productGroupIds)
                : null;
    }

    /** 삭제 상태 제외 조건 */
    public BooleanExpression statusNotDeleted() {
        return productJpaEntity.status.ne(ProductStatus.DELETED.name());
    }
}
