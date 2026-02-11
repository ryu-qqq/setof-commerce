package com.ryuqq.setof.storage.legacy.composite.web.banner.condition;

import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerEntity.legacyBannerEntity;
import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerItemEntity.legacyBannerItemEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.banner.entity.LegacyBannerEntity;
import com.ryuqq.setof.storage.legacy.banner.entity.LegacyBannerItemEntity;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBannerCompositeConditionBuilder - 레거시 Web 배너 Composite QueryDSL 조건 빌더.
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
public class LegacyWebBannerCompositeConditionBuilder {

    // ===== Banner 타입 조건 =====

    /**
     * 배너 타입 일치 조건.
     *
     * @param bannerType 배너 타입 문자열
     * @return BooleanExpression
     */
    public BooleanExpression bannerTypeEq(String bannerType) {
        if (bannerType == null || bannerType.isBlank()) {
            return null;
        }
        try {
            LegacyBannerEntity.BannerType type = LegacyBannerEntity.BannerType.valueOf(bannerType);
            return legacyBannerEntity.bannerType.eq(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // ===== Banner 상태 조건 =====

    /**
     * 배너 삭제 여부 N 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression bannerNotDeleted() {
        return legacyBannerEntity.deleteYn.eq(LegacyBannerEntity.Yn.N);
    }

    /**
     * 배너 표시 여부 Y 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression bannerDisplayed() {
        return legacyBannerEntity.displayYn.eq(LegacyBannerEntity.Yn.Y);
    }

    /**
     * 배너 표시 기간 내 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression bannerDisplayPeriodBetween() {
        LocalDateTime now = LocalDateTime.now();
        return legacyBannerEntity
                .displayStartDate
                .loe(now)
                .and(legacyBannerEntity.displayEndDate.goe(now));
    }

    // ===== BannerItem 상태 조건 =====

    /**
     * 배너 아이템 삭제 여부 N 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression bannerItemNotDeleted() {
        return legacyBannerItemEntity.deleteYn.eq(LegacyBannerItemEntity.Yn.N);
    }

    /**
     * 배너 아이템 표시 여부 Y 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression bannerItemDisplayed() {
        return legacyBannerItemEntity.displayYn.eq(LegacyBannerItemEntity.Yn.Y);
    }

    /**
     * 배너 아이템 표시 기간 내 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression bannerItemDisplayPeriodBetween() {
        LocalDateTime now = LocalDateTime.now();
        return legacyBannerItemEntity
                .displayStartDate
                .loe(now)
                .and(legacyBannerItemEntity.displayEndDate.goe(now));
    }

    // ===== 복합 조건 =====

    /**
     * 전시 중인 배너 조건 (삭제X + 표시O + 기간 내).
     *
     * @return BooleanExpression
     */
    public BooleanExpression onDisplayBanner() {
        return bannerNotDeleted().and(bannerDisplayed()).and(bannerDisplayPeriodBetween());
    }

    /**
     * 전시 중인 배너 아이템 조건 (삭제X + 표시O + 기간 내).
     *
     * @return BooleanExpression
     */
    public BooleanExpression onDisplayBannerItem() {
        return bannerItemNotDeleted()
                .and(bannerItemDisplayed())
                .and(bannerItemDisplayPeriodBetween());
    }
}
