package com.ryuqq.setof.adapter.out.persistence.banner.repository;

import static com.ryuqq.setof.adapter.out.persistence.banner.entity.QBannerGroupJpaEntity.bannerGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.banner.entity.QBannerSlideJpaEntity.bannerSlideJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.banner.condition.BannerConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * BannerSlideQueryDslRepository - 배너 슬라이드 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class BannerSlideQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final BannerConditionBuilder conditionBuilder;

    public BannerSlideQueryDslRepository(
            JPAQueryFactory queryFactory, BannerConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 배너 타입별 전시 중인 슬라이드 목록 조회.
     *
     * <p>banner_group과 banner_slide를 bannerGroupId로 조인하여, 배너 그룹이 활성·미삭제이고 슬라이드가 활성·미삭제인 항목을
     * displayOrder 오름차순으로 반환합니다.
     *
     * @param bannerType 배너 타입 문자열
     * @return BannerSlideJpaEntity 목록
     */
    public List<BannerSlideJpaEntity> fetchDisplaySlides(String bannerType) {
        return queryFactory
                .selectFrom(bannerSlideJpaEntity)
                .join(bannerGroupJpaEntity)
                .on(bannerSlideJpaEntity.bannerGroupId.eq(bannerGroupJpaEntity.id))
                .where(
                        conditionBuilder.bannerGroupTypeEq(bannerType),
                        conditionBuilder.bannerGroupActiveEq(true),
                        conditionBuilder.bannerGroupNotDeleted(),
                        conditionBuilder.bannerGroupDisplayPeriodBetween(),
                        conditionBuilder.bannerSlideActiveEq(true),
                        conditionBuilder.bannerSlideNotDeleted(),
                        conditionBuilder.bannerSlideDisplayPeriodBetween())
                .orderBy(bannerSlideJpaEntity.displayOrder.asc())
                .fetch();
    }
}
