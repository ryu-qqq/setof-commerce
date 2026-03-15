package com.ryuqq.setof.adapter.out.persistence.cart.repository;

import static com.ryuqq.setof.adapter.out.persistence.brand.entity.QBrandJpaEntity.brandJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.cart.entity.QCartItemJpaEntity.cartItemJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.category.entity.QCategoryJpaEntity.categoryJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.product.entity.QProductJpaEntity.productJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.product.entity.QProductOptionMappingJpaEntity.productOptionMappingJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity.productGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionGroupJpaEntity.sellerOptionGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionValueJpaEntity.sellerOptionValueJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.QProductGroupImageJpaEntity.productGroupImageJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerJpaEntity.sellerJpaEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.cart.condition.CartItemConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.cart.dto.CartFlatQueryDto;
import com.ryuqq.setof.adapter.out.persistence.cart.dto.CartOptionQueryDto;
import com.ryuqq.setof.adapter.out.persistence.cart.dto.CartQueryDto;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * CartCompositeQueryDslRepository - 장바구니 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * <p>9개 테이블 조인 (6 INNER + 3 LEFT) + flat 조회 후 Java groupBy로 옵션 집합 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class CartCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final CartItemConditionBuilder conditionBuilder;

    public CartCompositeQueryDslRepository(
            JPAQueryFactory queryFactory, CartItemConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 장바구니 목록 조회 (커서 페이징, 옵션 포함).
     *
     * <p>9개 테이블 조인:
     *
     * <ul>
     *   <li>INNER JOIN: product_groups, products, sellers, product_group_images, brand, category
     *   <li>LEFT JOIN: product_option_mappings, seller_option_values, seller_option_groups
     * </ul>
     *
     * @param criteria 검색 조건
     * @return 장바구니 목록
     */
    public List<CartQueryDto> fetchCarts(CartSearchCriteria criteria) {
        List<CartFlatQueryDto> flatRows =
                queryFactory
                        .select(
                                Projections.constructor(
                                        CartFlatQueryDto.class,
                                        cartItemJpaEntity.id,
                                        brandJpaEntity.id,
                                        brandJpaEntity.brandName,
                                        productGroupJpaEntity.id,
                                        productGroupJpaEntity.productGroupName,
                                        sellerJpaEntity.id,
                                        sellerJpaEntity.sellerName,
                                        productJpaEntity.id,
                                        productGroupJpaEntity.regularPrice,
                                        productGroupJpaEntity.currentPrice,
                                        productGroupJpaEntity.salePrice,
                                        productGroupJpaEntity.discountRate,
                                        cartItemJpaEntity.quantity,
                                        productJpaEntity.stockQuantity,
                                        productGroupImageJpaEntity.imageUrl,
                                        productJpaEntity.status,
                                        categoryJpaEntity.path,
                                        sellerOptionGroupJpaEntity.id,
                                        sellerOptionValueJpaEntity.id,
                                        sellerOptionGroupJpaEntity.optionGroupName,
                                        sellerOptionValueJpaEntity.optionValueName))
                        .from(cartItemJpaEntity)
                        .innerJoin(productGroupJpaEntity)
                        .on(productGroupJpaEntity.id.eq(cartItemJpaEntity.productGroupId))
                        .innerJoin(productJpaEntity)
                        .on(productJpaEntity.id.eq(cartItemJpaEntity.productId))
                        .innerJoin(sellerJpaEntity)
                        .on(sellerJpaEntity.id.eq(cartItemJpaEntity.sellerId))
                        .innerJoin(productGroupImageJpaEntity)
                        .on(
                                productGroupImageJpaEntity.productGroupId.eq(
                                        productGroupJpaEntity.id),
                                productGroupImageJpaEntity.imageType.eq("MAIN"),
                                productGroupImageJpaEntity.deletedAt.isNull())
                        .innerJoin(brandJpaEntity)
                        .on(brandJpaEntity.id.eq(productGroupJpaEntity.brandId))
                        .innerJoin(categoryJpaEntity)
                        .on(categoryJpaEntity.id.eq(productGroupJpaEntity.categoryId))
                        .leftJoin(productOptionMappingJpaEntity)
                        .on(
                                productOptionMappingJpaEntity.productId.eq(
                                        cartItemJpaEntity.productId),
                                productOptionMappingJpaEntity.deleted.eq(false))
                        .leftJoin(sellerOptionValueJpaEntity)
                        .on(
                                sellerOptionValueJpaEntity.id.eq(
                                        productOptionMappingJpaEntity.sellerOptionValueId),
                                sellerOptionValueJpaEntity.deleted.eq(false))
                        .leftJoin(sellerOptionGroupJpaEntity)
                        .on(
                                sellerOptionGroupJpaEntity.id.eq(
                                        sellerOptionValueJpaEntity.sellerOptionGroupId),
                                sellerOptionGroupJpaEntity.deleted.eq(false))
                        .where(
                                conditionBuilder.legacyUserIdEq(criteria.userId()),
                                conditionBuilder.notDeleted(),
                                conditionBuilder.cursorLessThan(criteria.cursor()))
                        .orderBy(cartItemJpaEntity.id.desc())
                        .fetch();

        return groupByCartId(flatRows, criteria.fetchSize());
    }

    /**
     * 장바구니 개수 조회.
     *
     * @param userId 사용자 ID
     * @return 장바구니 개수
     */
    public long countCarts(Long userId) {
        Long count =
                queryFactory
                        .select(cartItemJpaEntity.id.countDistinct())
                        .from(cartItemJpaEntity)
                        .where(
                                conditionBuilder.legacyUserIdEq(userId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    private List<CartQueryDto> groupByCartId(List<CartFlatQueryDto> flatRows, int limit) {
        Map<Long, CartQueryDto> grouped = new LinkedHashMap<>();

        for (CartFlatQueryDto row : flatRows) {
            CartQueryDto existing = grouped.get(row.cartId());
            if (existing == null) {
                if (grouped.size() >= limit) {
                    break;
                }
                Set<CartOptionQueryDto> options = new LinkedHashSet<>();
                addOptionIfPresent(options, row);
                existing =
                        new CartQueryDto(
                                row.cartId(),
                                row.brandId(),
                                row.brandName(),
                                row.productGroupId(),
                                row.productGroupName(),
                                row.sellerId(),
                                row.sellerName(),
                                row.productId(),
                                row.regularPrice(),
                                row.currentPrice(),
                                row.salePrice(),
                                row.discountRate(),
                                row.quantity(),
                                row.stockQuantity(),
                                row.imageUrl(),
                                row.productStatus(),
                                row.categoryPath(),
                                options);
                grouped.put(row.cartId(), existing);
            } else {
                addOptionIfPresent(existing.options(), row);
            }
        }

        return List.copyOf(grouped.values());
    }

    private void addOptionIfPresent(Set<CartOptionQueryDto> options, CartFlatQueryDto row) {
        if (row.optionGroupId() != null && row.optionValueId() != null) {
            options.add(
                    new CartOptionQueryDto(
                            row.optionGroupId(),
                            row.optionValueId(),
                            row.optionGroupName(),
                            row.optionValueName()));
        }
    }
}
