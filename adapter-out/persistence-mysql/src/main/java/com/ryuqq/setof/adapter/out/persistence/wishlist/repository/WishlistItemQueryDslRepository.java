package com.ryuqq.setof.adapter.out.persistence.wishlist.repository;

import static com.ryuqq.setof.adapter.out.persistence.brand.entity.QBrandJpaEntity.brandJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity.productGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.QProductGroupImageJpaEntity.productGroupImageJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.QProductGroupPriceJpaEntity.productGroupPriceJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.wishlist.entity.QWishlistItemJpaEntity.wishlistItemJpaEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.wishlist.condition.WishlistItemConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.wishlist.dto.WishlistItemQueryDto;
import com.ryuqq.setof.adapter.out.persistence.wishlist.entity.WishlistItemJpaEntity;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * WishlistItemQueryDslRepository - 찜 항목 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class WishlistItemQueryDslRepository {

    private static final String IMAGE_TYPE_MAIN = "MAIN";

    private final JPAQueryFactory queryFactory;
    private final WishlistItemConditionBuilder conditionBuilder;

    public WishlistItemQueryDslRepository(
            JPAQueryFactory queryFactory, WishlistItemConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 레거시 회원 ID + 상품 그룹 ID로 찜 항목 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_member_id에 매핑됩니다.
     *
     * @param legacyMemberId 레거시 회원 ID
     * @param productGroupId 상품 그룹 ID
     * @return 찜 항목 Optional
     */
    public Optional<WishlistItemJpaEntity> findByLegacyMemberIdAndProductGroupId(
            Long legacyMemberId, Long productGroupId) {
        WishlistItemJpaEntity entity =
                queryFactory
                        .selectFrom(wishlistItemJpaEntity)
                        .where(
                                conditionBuilder.legacyMemberIdEq(legacyMemberId),
                                conditionBuilder.productGroupIdEq(productGroupId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 레거시 회원 ID로 찜 항목 목록 조회.
     *
     * @param legacyMemberId 레거시 회원 ID
     * @return 찜 항목 목록
     */
    public List<WishlistItemJpaEntity> findAllByLegacyMemberId(Long legacyMemberId) {
        return queryFactory
                .selectFrom(wishlistItemJpaEntity)
                .where(
                        conditionBuilder.legacyMemberIdEq(legacyMemberId),
                        conditionBuilder.notDeleted())
                .fetch();
    }

    /**
     * 찜 목록 커서 기반 복합 조회.
     *
     * <p>wishlist_items INNER JOIN product_groups, product_group_images, brand. 커서(cursor) 기준 DESC
     * 정렬, fetchSize(size+1)로 hasNext 판단.
     *
     * @param criteria 검색 조건
     * @return 찜 항목 DTO 목록 (fetchSize만큼)
     */
    public List<WishlistItemQueryDto> fetchSlice(WishlistSearchCriteria criteria) {
        return queryFactory
                .select(
                        Projections.constructor(
                                WishlistItemQueryDto.class,
                                wishlistItemJpaEntity.id,
                                productGroupJpaEntity.id,
                                productGroupJpaEntity.sellerId,
                                productGroupJpaEntity.productGroupName,
                                brandJpaEntity.id,
                                brandJpaEntity.brandName,
                                productGroupImageJpaEntity.imageUrl,
                                productGroupJpaEntity.regularPrice,
                                productGroupJpaEntity.currentPrice,
                                productGroupPriceJpaEntity.discountRate,
                                productGroupJpaEntity.status,
                                brandJpaEntity.displayed,
                                wishlistItemJpaEntity.createdAt))
                .from(wishlistItemJpaEntity)
                .innerJoin(productGroupJpaEntity)
                .on(productGroupJpaEntity.id.eq(wishlistItemJpaEntity.productGroupId))
                .innerJoin(productGroupImageJpaEntity)
                .on(
                        productGroupImageJpaEntity.productGroupId.eq(productGroupJpaEntity.id),
                        productGroupImageJpaEntity.imageType.eq(IMAGE_TYPE_MAIN),
                        productGroupImageJpaEntity.deletedAt.isNull())
                .innerJoin(brandJpaEntity)
                .on(brandJpaEntity.id.eq(productGroupJpaEntity.brandId))
                .leftJoin(productGroupPriceJpaEntity)
                .on(productGroupPriceJpaEntity.productGroupId.eq(productGroupJpaEntity.id))
                .where(
                        conditionBuilder.legacyMemberIdEq(criteria.memberId().value()),
                        conditionBuilder.notDeleted(),
                        conditionBuilder.cursorLessThan(criteria.cursor()))
                .orderBy(wishlistItemJpaEntity.id.desc())
                .limit(criteria.fetchSize())
                .fetch();
    }

    /**
     * 레거시 회원 ID로 찜 항목 개수 조회.
     *
     * @param legacyMemberId 레거시 회원 ID
     * @return 찜 항목 개수
     */
    public long countByLegacyMemberId(Long legacyMemberId) {
        Long count =
                queryFactory
                        .select(wishlistItemJpaEntity.id.count())
                        .from(wishlistItemJpaEntity)
                        .where(
                                conditionBuilder.legacyMemberIdEq(legacyMemberId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
