package com.ryuqq.setof.storage.legacy.composite.web.gnb.condition;

import static com.ryuqq.setof.storage.legacy.gnb.entity.QLegacyGnbEntity.legacyGnbEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * LegacyWebGnbCompositeConditionBuilder - 레거시 Web GNB Composite QueryDSL 조건 빌더.
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
public class LegacyWebGnbCompositeConditionBuilder {

    // ===== GNB 상태 조건 =====

    /**
     * GNB 삭제 여부 N 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression gnbNotDeleted() {
        return legacyGnbEntity.deleteYn.eq(Yn.N);
    }

    /**
     * GNB 표시 여부 Y 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression gnbDisplayed() {
        return legacyGnbEntity.displayYn.eq(Yn.Y);
    }

    /**
     * GNB 표시 기간 내 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression gnbDisplayPeriodBetween() {
        LocalDateTime now = LocalDateTime.now();
        return legacyGnbEntity
                .displayStartDate
                .loe(now)
                .and(legacyGnbEntity.displayEndDate.goe(now));
    }

    // ===== 복합 조건 =====

    /**
     * 전시 중인 GNB 조건 (삭제X + 표시O + 기간 내).
     *
     * @return BooleanExpression
     */
    public BooleanExpression onDisplayGnb() {
        return gnbNotDeleted().and(gnbDisplayed()).and(gnbDisplayPeriodBetween());
    }
}
