package com.ryuqq.setof.adapter.out.persistence.seller.repository;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerAddressJpaEntity.sellerAddressJpaEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.seller.condition.SellerAddressConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.query.SellerAddressSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerAddressSortKey;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * SellerAddressQueryDslRepository - 셀러 주소 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class SellerAddressQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final SellerAddressConditionBuilder conditionBuilder;

    public SellerAddressQueryDslRepository(
            JPAQueryFactory queryFactory, SellerAddressConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 셀러 주소 조회.
     *
     * @param id 주소 ID
     * @return 주소 Optional
     */
    public Optional<SellerAddressJpaEntity> findById(Long id) {
        SellerAddressJpaEntity entity =
                queryFactory
                        .selectFrom(sellerAddressJpaEntity)
                        .where(sellerAddressJpaEntity.id.eq(id), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 셀러 ID로 모든 주소 조회.
     *
     * @param sellerId 셀러 ID
     * @return 주소 목록
     */
    public List<SellerAddressJpaEntity> findAllBySellerId(Long sellerId) {
        return queryFactory
                .selectFrom(sellerAddressJpaEntity)
                .where(conditionBuilder.sellerIdEq(sellerId), conditionBuilder.notDeleted())
                .orderBy(sellerAddressJpaEntity.createdAt.desc())
                .fetch();
    }

    /**
     * 셀러 ID + 주소 유형으로 기본 주소 조회.
     *
     * @param sellerId 셀러 ID
     * @param addressType 주소 유형
     * @return 기본 주소 Optional
     */
    public Optional<SellerAddressJpaEntity> findDefaultBySellerId(
            Long sellerId, AddressType addressType) {
        SellerAddressJpaEntity entity =
                queryFactory
                        .selectFrom(sellerAddressJpaEntity)
                        .where(
                                conditionBuilder.sellerIdEq(sellerId),
                                conditionBuilder.addressTypeEq(addressType),
                                conditionBuilder.defaultAddressEq(true),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 셀러 ID 존재 여부 확인.
     *
     * @param sellerId 셀러 ID
     * @return 존재 여부
     */
    public boolean existsBySellerId(Long sellerId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(sellerAddressJpaEntity)
                        .where(conditionBuilder.sellerIdEq(sellerId), conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 셀러 ID + 주소 유형 + 주소명 존재 여부 확인.
     *
     * @param sellerId 셀러 ID
     * @param addressType 주소 유형
     * @param addressName 주소명
     * @return 존재 여부
     */
    public boolean existsBySellerIdAndAddressTypeAndAddressName(
            Long sellerId, AddressType addressType, String addressName) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(sellerAddressJpaEntity)
                        .where(
                                conditionBuilder.sellerIdEq(sellerId),
                                conditionBuilder.addressTypeEq(addressType),
                                conditionBuilder.addressNameEq(addressName),
                                conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 검색 조건으로 셀러 주소 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 주소 목록
     */
    public List<SellerAddressJpaEntity> search(SellerAddressSearchCriteria criteria) {
        QueryContext<SellerAddressSortKey> qc = criteria.queryContext();

        return queryFactory
                .selectFrom(sellerAddressJpaEntity)
                .where(
                        conditionBuilder.sellerIdIn(criteria.sellerIds()),
                        conditionBuilder.addressTypeIn(criteria.addressTypes()),
                        conditionBuilder.defaultAddressEq(criteria.defaultAddress()),
                        conditionBuilder.searchCondition(criteria),
                        conditionBuilder.notDeleted())
                .orderBy(createOrderSpecifier(qc.sortKey(), qc.sortDirection()))
                .offset(criteria.offset())
                .limit(criteria.size())
                .fetch();
    }

    /**
     * 검색 조건으로 셀러 주소 개수 조회.
     *
     * @param criteria 검색 조건
     * @return 주소 개수
     */
    public long count(SellerAddressSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(sellerAddressJpaEntity.count())
                        .from(sellerAddressJpaEntity)
                        .where(
                                conditionBuilder.sellerIdIn(criteria.sellerIds()),
                                conditionBuilder.addressTypeIn(criteria.addressTypes()),
                                conditionBuilder.defaultAddressEq(criteria.defaultAddress()),
                                conditionBuilder.searchCondition(criteria),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    private OrderSpecifier<?> createOrderSpecifier(
            SellerAddressSortKey sortKey, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc
                            ? sellerAddressJpaEntity.createdAt.asc()
                            : sellerAddressJpaEntity.createdAt.desc();
        };
    }
}
