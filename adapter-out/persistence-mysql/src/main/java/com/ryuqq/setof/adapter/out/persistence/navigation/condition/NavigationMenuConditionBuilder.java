package com.ryuqq.setof.adapter.out.persistence.navigation.condition;

import static com.ryuqq.setof.adapter.out.persistence.navigation.entity.QNavigationMenuJpaEntity.navigationMenuJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuConditionBuilder - 네비게이션 메뉴 QueryDSL 조건 빌더.
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
public class NavigationMenuConditionBuilder {

    /** 활성 여부 일치 조건 */
    public BooleanExpression activeEq(Boolean active) {
        return active != null ? navigationMenuJpaEntity.active.eq(active) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return navigationMenuJpaEntity.deletedAt.isNull();
    }

    /** 전시 기간 조건 (displayStartAt <= now <= displayEndAt) */
    public BooleanExpression displayPeriodBetween() {
        Instant now = Instant.now();
        return navigationMenuJpaEntity
                .displayStartAt
                .loe(now)
                .and(navigationMenuJpaEntity.displayEndAt.goe(now));
    }
}
