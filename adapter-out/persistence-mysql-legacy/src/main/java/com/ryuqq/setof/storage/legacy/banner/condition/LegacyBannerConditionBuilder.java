package com.ryuqq.setof.storage.legacy.banner.condition;

import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerEntity.legacyBannerEntity;
import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerItemEntity.legacyBannerItemEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.banner.entity.LegacyBannerEntity;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * LegacyBannerConditionBuilder - 레거시 배너 QueryDSL 조건 빌더.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyBannerConditionBuilder {

    public BooleanExpression bannerTypeEq(String bannerType) {
        if (bannerType == null || bannerType.isBlank()) {
            return null;
        }
        return legacyBannerEntity.bannerType.eq(LegacyBannerEntity.BannerType.valueOf(bannerType));
    }

    public BooleanExpression bannerNotDeleted() {
        return legacyBannerEntity.deleteYn.eq(Yn.N);
    }

    public BooleanExpression bannerDisplayed() {
        return legacyBannerEntity.displayYn.eq(Yn.Y);
    }

    public BooleanExpression bannerDisplayPeriodBetween() {
        LocalDateTime now = LocalDateTime.now();
        return legacyBannerEntity
                .displayStartDate
                .loe(now)
                .and(legacyBannerEntity.displayEndDate.goe(now));
    }

    public BooleanExpression bannerItemNotDeleted() {
        return legacyBannerItemEntity.deleteYn.eq(Yn.N);
    }

    public BooleanExpression bannerItemDisplayed() {
        return legacyBannerItemEntity.displayYn.eq(Yn.Y);
    }

    public BooleanExpression bannerItemDisplayPeriodBetween() {
        LocalDateTime now = LocalDateTime.now();
        return legacyBannerItemEntity
                .displayStartDate
                .loe(now)
                .and(legacyBannerItemEntity.displayEndDate.goe(now));
    }

    /** 전시 중인 배너 조건 (삭제X + 표시O + 기간 내). */
    public BooleanExpression onDisplayBanner() {
        return bannerNotDeleted().and(bannerDisplayed()).and(bannerDisplayPeriodBetween());
    }

    /** 전시 중인 배너 아이템 조건 (삭제X + 표시O + 기간 내). */
    public BooleanExpression onDisplayBannerItem() {
        return bannerItemNotDeleted()
                .and(bannerItemDisplayed())
                .and(bannerItemDisplayPeriodBetween());
    }
}
