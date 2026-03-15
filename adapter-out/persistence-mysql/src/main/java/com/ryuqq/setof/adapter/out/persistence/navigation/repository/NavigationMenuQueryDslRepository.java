package com.ryuqq.setof.adapter.out.persistence.navigation.repository;

import static com.ryuqq.setof.adapter.out.persistence.navigation.entity.QNavigationMenuJpaEntity.navigationMenuJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.navigation.condition.NavigationMenuConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * NavigationMenuQueryDslRepository - 네비게이션 메뉴 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class NavigationMenuQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final NavigationMenuConditionBuilder conditionBuilder;

    public NavigationMenuQueryDslRepository(
            JPAQueryFactory queryFactory, NavigationMenuConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 전시 중인 네비게이션 메뉴 목록 조회.
     *
     * <p>active=true, deleted_at IS NULL 조건으로 필터링하고 display_order ASC 정렬.
     *
     * @return NavigationMenuJpaEntity 목록
     */
    /**
     * ID로 네비게이션 메뉴 단건 조회.
     *
     * @param id 네비게이션 메뉴 ID
     * @return NavigationMenuJpaEntity Optional
     */
    public Optional<NavigationMenuJpaEntity> findById(long id) {
        NavigationMenuJpaEntity entity =
                queryFactory
                        .selectFrom(navigationMenuJpaEntity)
                        .where(navigationMenuJpaEntity.id.eq(id), conditionBuilder.notDeleted())
                        .fetchFirst();
        return Optional.ofNullable(entity);
    }

    public List<NavigationMenuJpaEntity> fetchDisplayMenus() {
        return queryFactory
                .selectFrom(navigationMenuJpaEntity)
                .where(
                        conditionBuilder.activeEq(true),
                        conditionBuilder.notDeleted(),
                        conditionBuilder.displayPeriodBetween())
                .orderBy(navigationMenuJpaEntity.displayOrder.asc())
                .fetch();
    }

    /**
     * 검색 조건으로 네비게이션 메뉴 목록 조회.
     *
     * @param displayStartAfter 전시 시작일 이후 (nullable)
     * @param displayEndBefore 전시 종료일 이전 (nullable)
     * @return NavigationMenuJpaEntity 목록
     */
    public List<NavigationMenuJpaEntity> searchMenus(
            Instant displayStartAfter, Instant displayEndBefore) {

        return queryFactory
                .selectFrom(navigationMenuJpaEntity)
                .where(
                        conditionBuilder.notDeleted(),
                        conditionBuilder.displayStartAfter(displayStartAfter),
                        conditionBuilder.displayEndBefore(displayEndBefore))
                .orderBy(navigationMenuJpaEntity.displayOrder.asc())
                .fetch();
    }
}
