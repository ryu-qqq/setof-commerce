package com.setof.connectly.module.display.repository.component;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.display.entity.component.QComponentItem.componentItem;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;
import static com.setof.connectly.module.search.entity.QProductScore.productScore;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.common.repository.AbstractCommonRepository;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.QProductGroupThumbnail;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractComponentRepository extends AbstractCommonRepository {

    protected final JPAQueryFactory queryFactory;

    public List<ProductGroupThumbnail> fetchProductComponentsWhenLesserThanExposedSize(
            long componentId, Set<Long> productIds, ComponentFilter filter, int pageSize) {
        List<OrderSpecifier<?>> orders =
                createOrderSpecifiersFromPageable(productGroup, filter.getOrderType());

        BooleanExpression dynamicWhere = createDynamicWhereCondition(filter, filter.getOrderType());

        return queryFactory
                .from(productGroup)
                .innerJoin(productGroupImage)
                .on(productGroupImage.productGroup.id.eq(productGroup.id))
                .on(
                        productGroupImage.imageDetail.productGroupImageType.eq(
                                ProductGroupImageType.MAIN))
                .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(brand)
                .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .leftJoin(productRatingStats)
                .on(productRatingStats.id.eq(productGroup.id))
                .leftJoin(productScore)
                .on(productScore.id.eq(productGroup.id))
                .where(
                        onSaleProduct(),
                        dynamicWhere,
                        brandIdIn(filter),
                        brandIdEq(filter.getBrandId()),
                        categoryIdIn(filter),
                        productGroupIdNotIn(productIds),
                        betweenPrice(filter))
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .limit(pageSize + 1)
                .transform(
                        GroupBy.groupBy(productGroup.id)
                                .list(
                                        new QProductGroupThumbnail(
                                                productGroup.id,
                                                productGroup.productGroupDetails.sellerId,
                                                productGroup.productGroupDetails.productGroupName,
                                                new QBrandDto(brand.id, brand.brandName),
                                                productGroupImage.imageDetail.imageUrl,
                                                productGroup.productGroupDetails.price,
                                                productGroup.insertDate,
                                                productRatingStats.averageRating.coalesce(0.0),
                                                productRatingStats.reviewCount.coalesce(0L),
                                                productScore.score.coalesce(0.0),
                                                productGroup.productGroupDetails.productStatus)));
    }

    protected List<ProductGroupThumbnail> getProductGroupThumbnail(List<Long> componentItemIds) {
        return queryFactory
                .from(componentItem)
                .innerJoin(productGroup)
                .on(componentItem.productGroupId.eq(productGroup.id))
                .innerJoin(productGroupImage)
                .on(productGroupImage.productGroup.id.eq(productGroup.id))
                .on(
                        productGroupImage.imageDetail.productGroupImageType.eq(
                                ProductGroupImageType.MAIN))
                .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(brand)
                .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .leftJoin(productRatingStats)
                .on(productRatingStats.id.eq(productGroup.id))
                .leftJoin(productScore)
                .on(productScore.id.eq(productGroup.id))
                .where(componentItemIdIn(componentItemIds))
                .orderBy(componentItem.id.asc())
                .transform(
                        GroupBy.groupBy(componentItem.id)
                                .list(
                                        new QProductGroupThumbnail(
                                                productGroup.id,
                                                productGroup.productGroupDetails.sellerId,
                                                componentItem.productDisplayName.coalesce(
                                                        productGroup
                                                                .productGroupDetails
                                                                .productGroupName),
                                                new QBrandDto(
                                                        brand.id.as("brandId"), brand.brandName),
                                                productGroupImage.imageDetail.imageUrl,
                                                productGroup.productGroupDetails.price,
                                                productGroup.insertDate,
                                                productRatingStats.averageRating.coalesce(0.0),
                                                productRatingStats.reviewCount.coalesce(0L),
                                                productScore.score.coalesce(0.0),
                                                productGroup.productGroupDetails.productStatus)));
    }

    protected BooleanExpression componentItemIdIn(List<Long> componentItemIds) {
        if (componentItemIds.size() > 0) return componentItem.id.in(componentItemIds);
        else return null;
    }

    protected BooleanExpression onSaleProduct() {
        return productGroup.productGroupDetails.productStatus.displayYn.eq(Yn.Y);
    }

    protected BooleanExpression brandIdIn(ComponentFilter filter) {
        if (filter.getBrandIds() != null) {
            List<Long> collect =
                    filter.getBrandIds().stream()
                            .filter(aLong -> aLong > 0)
                            .collect(Collectors.toList());
            if (!collect.isEmpty())
                return productGroup.productGroupDetails.brandId.in(filter.getBrandIds());
            return null;
        }
        return null;
    }

    protected BooleanExpression brandIdEq(Long brandId) {
        if (brandId != null) return productGroup.productGroupDetails.brandId.eq(brandId);
        return null;
    }

    protected BooleanExpression categoryIdIn(ComponentFilter filter) {
        List<Long> categoryIds = filter.getCategoryIds();
        if (categoryIds == null) return null;
        if (categoryIds.isEmpty()) return null;
        return productGroup.productGroupDetails.categoryId.in(filter.getCategoryIds());
    }

    protected BooleanExpression productGroupIdNotIn(Set<Long> productIds) {
        if (!productIds.isEmpty()) return productGroup.id.notIn(productIds);
        else return null;
    }

    protected BooleanExpression betweenPrice(ComponentFilter filter) {
        if (filter.getHighestPrice() != null || filter.getLowestPrice() != null) {
            Long highestPrice = filter.getHighestPrice();
            Long lowestPrice = filter.getLowestPrice();

            if (highestPrice != null && lowestPrice != null) {
                return productGroup.productGroupDetails.price.salePrice.between(
                        lowestPrice, highestPrice);
            } else if (highestPrice != null) {
                return productGroup.productGroupDetails.price.salePrice.goe(highestPrice);
            } else if (lowestPrice != null) {
                return productGroup.productGroupDetails.price.salePrice.loe(lowestPrice);
            }
        }

        return null;
    }
}
