package com.ryuqq.setof.adapter.out.persistence.banner.repository;

import static com.ryuqq.setof.adapter.out.persistence.banner.entity.QBannerGroupJpaEntity.bannerGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.banner.entity.QBannerSlideJpaEntity.bannerSlideJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.banner.condition.BannerConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * BannerSlideQueryDslRepository - 배너 슬라이드 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>PER-REP-005: 그룹 ID 기반 단건 조회 및 슬라이드 목록 조회 메서드 제공.
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

    /**
     * ID로 배너 그룹 단건 조회.
     *
     * @param id 배너 그룹 ID
     * @return BannerGroupJpaEntity (없으면 null)
     */
    public BannerGroupJpaEntity findBannerGroupById(long id) {
        return queryFactory
                .selectFrom(bannerGroupJpaEntity)
                .where(bannerGroupJpaEntity.id.eq(id))
                .fetchOne();
    }

    /**
     * 배너 그룹 ID에 속한 슬라이드 목록 조회 (displayOrder 오름차순).
     *
     * @param bannerGroupId 배너 그룹 ID
     * @return BannerSlideJpaEntity 목록
     */
    public List<BannerSlideJpaEntity> findSlidesByGroupId(long bannerGroupId) {
        return queryFactory
                .selectFrom(bannerSlideJpaEntity)
                .where(bannerSlideJpaEntity.bannerGroupId.eq(bannerGroupId))
                .orderBy(bannerSlideJpaEntity.displayOrder.asc())
                .fetch();
    }

    /**
     * 검색 조건으로 배너 그룹 목록 조회 (페이징).
     *
     * @param bannerType 배너 타입 (nullable)
     * @param active 활성 여부 (nullable)
     * @param displayStartAfter 전시 시작일 이후 (nullable)
     * @param displayEndBefore 전시 종료일 이전 (nullable)
     * @param titleKeyword 제목 검색어 (nullable)
     * @param lastDomainId No-Offset 마지막 ID (nullable)
     * @param offset SQL offset
     * @param limit SQL limit
     * @param isNoOffset No-Offset 페이징 여부
     * @return BannerGroupJpaEntity 목록
     */
    public List<BannerGroupJpaEntity> searchBannerGroups(
            String bannerType,
            Boolean active,
            Instant displayStartAfter,
            Instant displayEndBefore,
            String titleKeyword,
            Long lastDomainId,
            long offset,
            int limit,
            boolean isNoOffset) {

        var query =
                queryFactory
                        .selectFrom(bannerGroupJpaEntity)
                        .where(
                                conditionBuilder.bannerGroupTypeEq(bannerType),
                                conditionBuilder.bannerGroupActiveEq(active),
                                conditionBuilder.bannerGroupNotDeleted(),
                                conditionBuilder.bannerGroupDisplayStartAfter(displayStartAfter),
                                conditionBuilder.bannerGroupDisplayEndBefore(displayEndBefore),
                                conditionBuilder.bannerGroupTitleContains(titleKeyword),
                                conditionBuilder.bannerGroupIdLt(lastDomainId))
                        .orderBy(bannerGroupJpaEntity.id.desc())
                        .limit(limit);

        if (!isNoOffset) {
            query.offset(offset);
        }

        return query.fetch();
    }

    /**
     * 검색 조건으로 배너 그룹 총 건수 조회.
     *
     * @param bannerType 배너 타입 (nullable)
     * @param active 활성 여부 (nullable)
     * @param displayStartAfter 전시 시작일 이후 (nullable)
     * @param displayEndBefore 전시 종료일 이전 (nullable)
     * @param titleKeyword 제목 검색어 (nullable)
     * @return 총 건수
     */
    public long countBannerGroups(
            String bannerType,
            Boolean active,
            Instant displayStartAfter,
            Instant displayEndBefore,
            String titleKeyword) {

        Long count =
                queryFactory
                        .select(bannerGroupJpaEntity.count())
                        .from(bannerGroupJpaEntity)
                        .where(
                                conditionBuilder.bannerGroupTypeEq(bannerType),
                                conditionBuilder.bannerGroupActiveEq(active),
                                conditionBuilder.bannerGroupNotDeleted(),
                                conditionBuilder.bannerGroupDisplayStartAfter(displayStartAfter),
                                conditionBuilder.bannerGroupDisplayEndBefore(displayEndBefore),
                                conditionBuilder.bannerGroupTitleContains(titleKeyword))
                        .fetchOne();

        return count != null ? count : 0L;
    }
}
