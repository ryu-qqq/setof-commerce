package com.ryuqq.setof.storage.legacy.composite.content.condition;

import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyComponentEntity.legacyComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyContentEntity.legacyContentEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.content.entity.LegacyComponentEntity;
import com.ryuqq.setof.storage.legacy.content.entity.LegacyContentEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebContentCompositeConditionBuilder - 레거시 웹 콘텐츠 Composite QueryDSL 조건 빌더.
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
public class LegacyWebContentCompositeConditionBuilder {

    // ===== Content ID 조건 =====

    /**
     * 콘텐츠 ID 일치 조건.
     *
     * @param contentId 콘텐츠 ID
     * @return BooleanExpression
     */
    public BooleanExpression contentIdEq(Long contentId) {
        return contentId != null ? legacyContentEntity.id.eq(contentId) : null;
    }

    /**
     * 콘텐츠 ID 목록 포함 조건.
     *
     * @param contentIds 콘텐츠 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression contentIdIn(List<Long> contentIds) {
        return contentIds != null && !contentIds.isEmpty()
                ? legacyContentEntity.id.in(contentIds)
                : null;
    }

    // ===== Content 상태 조건 =====

    /**
     * 콘텐츠 삭제 여부 N 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression contentNotDeleted() {
        return legacyContentEntity.deleteYn.eq(LegacyContentEntity.Yn.N);
    }

    /**
     * 콘텐츠 표시 여부 Y 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression contentDisplayed() {
        return legacyContentEntity.displayYn.eq(LegacyContentEntity.Yn.Y);
    }

    /**
     * 콘텐츠 표시 기간 내 조건 (bypass = false 일 때).
     *
     * @param bypass 전시 기간 체크 우회 여부
     * @return BooleanExpression
     */
    public BooleanExpression contentDisplayPeriodBetween(boolean bypass) {
        if (bypass) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return legacyContentEntity
                .displayStartDate
                .loe(now)
                .and(legacyContentEntity.displayEndDate.goe(now));
    }

    // ===== Component ID 조건 =====

    /**
     * 컴포넌트 ID 일치 조건.
     *
     * @param componentId 컴포넌트 ID
     * @return BooleanExpression
     */
    public BooleanExpression componentIdEq(Long componentId) {
        return componentId != null ? legacyComponentEntity.id.eq(componentId) : null;
    }

    /**
     * 컴포넌트가 특정 콘텐츠에 속하는 조건.
     *
     * @param contentId 콘텐츠 ID
     * @return BooleanExpression
     */
    public BooleanExpression componentContentIdEq(Long contentId) {
        return contentId != null ? legacyComponentEntity.contentId.eq(contentId) : null;
    }

    // ===== Component 상태 조건 =====

    /**
     * 컴포넌트 삭제 여부 N 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression componentNotDeleted() {
        return legacyComponentEntity.deleteYn.eq(LegacyComponentEntity.Yn.N);
    }

    /**
     * 컴포넌트 표시 여부 Y 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression componentDisplayed() {
        return legacyComponentEntity.displayYn.eq(LegacyComponentEntity.Yn.Y);
    }

    /**
     * 컴포넌트 표시 기간 내 조건 (bypass = false 일 때).
     *
     * @param bypass 전시 기간 체크 우회 여부
     * @return BooleanExpression
     */
    public BooleanExpression componentDisplayPeriodBetween(boolean bypass) {
        if (bypass) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return legacyComponentEntity
                .displayStartDate
                .loe(now)
                .and(legacyComponentEntity.displayEndDate.goe(now));
    }

    // ===== 복합 조건 =====

    /**
     * 전시 중인 콘텐츠 조건 (삭제X + 표시O + 기간 내).
     *
     * @param bypass 전시 기간 체크 우회 여부
     * @return BooleanExpression
     */
    public BooleanExpression onDisplayContent(boolean bypass) {
        BooleanExpression base = contentNotDeleted().and(contentDisplayed());
        BooleanExpression period = contentDisplayPeriodBetween(bypass);
        return period != null ? base.and(period) : base;
    }

    /**
     * 전시 중인 컴포넌트 조건 (삭제X + 표시O + 기간 내).
     *
     * @param bypass 전시 기간 체크 우회 여부
     * @return BooleanExpression
     */
    public BooleanExpression onDisplayComponent(boolean bypass) {
        BooleanExpression base = componentNotDeleted().and(componentDisplayed());
        BooleanExpression period = componentDisplayPeriodBetween(bypass);
        return period != null ? base.and(period) : base;
    }
}
