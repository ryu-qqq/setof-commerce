package com.ryuqq.setof.adapter.out.persistence.order.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.order.condition.OrderSearchCondition;
import com.ryuqq.setof.adapter.out.persistence.order.condition.OrderSearchCondition.SortField;
import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.order.entity.QOrderJpaEntity;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * OrderQueryDslRepository - Order QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(UUID id): ID로 단건 조회
 *   <li>findByOrderNumber(String orderNumber): 주문번호로 단건 조회
 *   <li>findByCondition(OrderSearchCondition): 검색 조건으로 목록 조회
 *   <li>countByCondition(OrderSearchCondition): 검색 조건으로 개수 조회
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>동적 쿼리 구성 (BooleanExpression)
 *   <li>단건/목록 조회
 *   <li>정렬 처리
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지 (fetch join, left join, inner join)
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class OrderQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QOrderJpaEntity qOrder = QOrderJpaEntity.orderJpaEntity;

    public OrderQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Order 단건 조회
     *
     * @param id Order ID (UUID)
     * @return OrderJpaEntity (Optional)
     */
    public Optional<OrderJpaEntity> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qOrder).where(qOrder.id.eq(id)).fetchOne());
    }

    /**
     * 주문 번호로 Order 단건 조회
     *
     * @param orderNumber 주문 번호
     * @return OrderJpaEntity (Optional)
     */
    public Optional<OrderJpaEntity> findByOrderNumber(String orderNumber) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qOrder)
                        .where(qOrder.orderNumber.eq(orderNumber))
                        .fetchOne());
    }

    /**
     * ID로 Order 존재 여부 확인
     *
     * @param id Order ID (UUID)
     * @return 존재 여부
     */
    public boolean existsById(UUID id) {
        Integer count = queryFactory.selectOne().from(qOrder).where(qOrder.id.eq(id)).fetchFirst();

        return count != null;
    }

    /**
     * Legacy Order ID로 Order 단건 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 조회 메서드
     *
     * @param legacyOrderId Legacy Order ID (Long)
     * @return OrderJpaEntity (Optional)
     */
    public Optional<OrderJpaEntity> findByLegacyOrderId(Long legacyOrderId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qOrder)
                        .where(qOrder.legacyOrderId.eq(legacyOrderId))
                        .fetchOne());
    }

    /**
     * 검색 조건으로 Order 목록 조회 (커서 기반 페이징)
     *
     * <p>User API와 Admin API 통합 지원
     *
     * @param condition 검색 조건
     * @return OrderJpaEntity 목록
     */
    public List<OrderJpaEntity> findByCondition(OrderSearchCondition condition) {
        BooleanBuilder builder = buildConditions(condition);
        OrderSpecifier<?>[] orderSpecifiers = buildOrderSpecifiers(condition);

        return queryFactory
                .selectFrom(qOrder)
                .where(builder)
                .orderBy(orderSpecifiers)
                .limit(condition.limit())
                .fetch();
    }

    /**
     * 검색 조건으로 Order 개수 조회
     *
     * @param condition 검색 조건
     * @return 주문 개수
     */
    public long countByCondition(OrderSearchCondition condition) {
        BooleanBuilder builder = buildConditions(condition);

        Long count = queryFactory.select(qOrder.count()).from(qOrder).where(builder).fetchOne();

        return count != null ? count : 0L;
    }

    /**
     * 회원 ID와 상태별 주문 개수 조회
     *
     * @param memberId 회원 ID
     * @param statuses 조회할 상태 목록
     * @return 상태별 개수 Map
     */
    public Map<String, Long> countOrderStatusesByMemberId(String memberId, List<String> statuses) {
        return queryFactory
                .select(qOrder.status, qOrder.count())
                .from(qOrder)
                .where(
                        qOrder.memberId.eq(memberId),
                        statuses != null && !statuses.isEmpty() ? qOrder.status.in(statuses) : null)
                .groupBy(qOrder.status)
                .fetch()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                tuple -> tuple.get(qOrder.status),
                                tuple -> tuple.get(qOrder.count())));
    }

    /**
     * 검색 조건 BooleanBuilder 생성
     *
     * <p>User API (memberId)와 Admin API (sellerId, searchKeyword) 통합 지원
     *
     * @param condition 검색 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder buildConditions(OrderSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

        // User API: 회원 ID 조건
        if (condition.hasMemberId()) {
            builder.and(qOrder.memberId.eq(condition.memberId()));
        }

        // Admin API: 셀러 ID 조건
        if (condition.hasSellerId()) {
            builder.and(qOrder.sellerId.eq(condition.sellerId()));
        }

        // 상태 필터
        if (condition.hasStatuses()) {
            builder.and(qOrder.status.in(condition.statuses()));
        }

        // Admin API: 검색어 (주문번호, 수령인 이름)
        if (condition.hasSearchKeyword()) {
            BooleanBuilder keywordBuilder = new BooleanBuilder();
            keywordBuilder.or(qOrder.orderNumber.containsIgnoreCase(condition.searchKeyword()));
            keywordBuilder.or(qOrder.receiverName.containsIgnoreCase(condition.searchKeyword()));
            builder.and(keywordBuilder);
        }

        // 기간 필터
        if (condition.hasStartDate()) {
            builder.and(qOrder.orderedAt.goe(condition.startDate()));
        }

        if (condition.hasEndDate()) {
            builder.and(qOrder.orderedAt.loe(condition.endDate()));
        }

        // 커서 기반 페이징
        if (condition.hasCursor()) {
            if (condition.sortAscending()) {
                builder.and(qOrder.id.gt(condition.lastOrderId()));
            } else {
                builder.and(qOrder.id.lt(condition.lastOrderId()));
            }
        }

        return builder;
    }

    /**
     * 정렬 조건 생성
     *
     * @param condition 검색 조건
     * @return OrderSpecifier 배열
     */
    private OrderSpecifier<?>[] buildOrderSpecifiers(OrderSearchCondition condition) {
        SortField sortField = condition.sortField();
        boolean ascending = condition.sortAscending();

        OrderSpecifier<?> primarySort = createOrderSpecifier(sortField, ascending);
        OrderSpecifier<?> secondarySort = ascending ? qOrder.id.asc() : qOrder.id.desc();

        return new OrderSpecifier<?>[] {primarySort, secondarySort};
    }

    /**
     * 정렬 필드별 OrderSpecifier 생성
     *
     * @param sortField 정렬 필드
     * @param ascending 오름차순 여부
     * @return OrderSpecifier
     */
    private OrderSpecifier<?> createOrderSpecifier(SortField sortField, boolean ascending) {
        return switch (sortField) {
            case ID -> ascending ? qOrder.id.asc() : qOrder.id.desc();
            case ORDER_DATE -> ascending ? qOrder.orderedAt.asc() : qOrder.orderedAt.desc();
            case CREATED_AT -> ascending ? qOrder.createdAt.asc() : qOrder.createdAt.desc();
            case UPDATED_AT -> ascending ? qOrder.updatedAt.asc() : qOrder.updatedAt.desc();
            case TOTAL_AMOUNT -> ascending ? qOrder.totalAmount.asc() : qOrder.totalAmount.desc();
        };
    }
}
