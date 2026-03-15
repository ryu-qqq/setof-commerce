package com.ryuqq.setof.adapter.out.persistence.banner.condition;

import static com.ryuqq.setof.adapter.out.persistence.banner.entity.QBannerGroupJpaEntity.bannerGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.banner.entity.QBannerSlideJpaEntity.bannerSlideJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * BannerConditionBuilder - 배너 QueryDSL 조건 빌더.
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
public class BannerConditionBuilder {

    /** 배너 그룹 타입 일치 조건 */
    public BooleanExpression bannerGroupTypeEq(String bannerType) {
        return bannerType != null && !bannerType.isBlank()
                ? bannerGroupJpaEntity.bannerType.eq(bannerType)
                : null;
    }

    /** 배너 그룹 활성 여부 일치 조건 */
    public BooleanExpression bannerGroupActiveEq(boolean active) {
        return bannerGroupJpaEntity.active.eq(active);
    }

    /** 배너 그룹 삭제되지 않은 조건 */
    public BooleanExpression bannerGroupNotDeleted() {
        return bannerGroupJpaEntity.deletedAt.isNull();
    }

    /** 배너 슬라이드 활성 여부 일치 조건 */
    public BooleanExpression bannerSlideActiveEq(boolean active) {
        return bannerSlideJpaEntity.active.eq(active);
    }

    /** 배너 슬라이드 삭제되지 않은 조건 */
    public BooleanExpression bannerSlideNotDeleted() {
        return bannerSlideJpaEntity.deletedAt.isNull();
    }

    /** 배너 그룹 전시 기간 조건 (displayStartAt <= now <= displayEndAt) */
    public BooleanExpression bannerGroupDisplayPeriodBetween() {
        Instant now = Instant.now();
        return bannerGroupJpaEntity
                .displayStartAt
                .loe(now)
                .and(bannerGroupJpaEntity.displayEndAt.goe(now));
    }

    /** 배너 슬라이드 전시 기간 조건 (displayStartAt <= now <= displayEndAt) */
    public BooleanExpression bannerSlideDisplayPeriodBetween() {
        Instant now = Instant.now();
        return bannerSlideJpaEntity
                .displayStartAt
                .loe(now)
                .and(bannerSlideJpaEntity.displayEndAt.goe(now));
    }
}
