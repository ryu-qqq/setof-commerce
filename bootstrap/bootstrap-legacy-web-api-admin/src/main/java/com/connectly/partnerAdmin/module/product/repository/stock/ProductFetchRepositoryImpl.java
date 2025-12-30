package com.connectly.partnerAdmin.module.product.repository.stock;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchDto;
import com.connectly.partnerAdmin.module.product.dto.QProductFetchDto;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.connectly.partnerAdmin.module.product.entity.delivery.QProductDelivery.productDelivery;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupDetailDescription.productGroupDetailDescription;
import static com.connectly.partnerAdmin.module.product.entity.notice.QProductNotice.productNotice;
import static com.connectly.partnerAdmin.module.product.entity.option.QOptionDetail.optionDetail;
import static com.connectly.partnerAdmin.module.product.entity.option.QOptionGroup.optionGroup;
import static com.connectly.partnerAdmin.module.product.entity.option.QProductOption.productOption;
import static com.connectly.partnerAdmin.module.product.entity.score.QProductRatingStats.productRatingStats;
import static com.connectly.partnerAdmin.module.product.entity.score.QProductScore.productScore;
import static com.connectly.partnerAdmin.module.product.entity.stock.QProduct.product;
import static com.connectly.partnerAdmin.module.product.entity.stock.QProductStock.productStock;

@Repository
@RequiredArgsConstructor
public class ProductFetchRepositoryImpl implements ProductFetchRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Product> fetchProductEntity(long productId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(product)
                        .join(product.productGroup, productGroup).fetchJoin()
                        .join(productGroup.productDelivery, productDelivery).fetchJoin()
                        .join(productGroup.productNotice, productNotice).fetchJoin()
                        .join(productGroup.detailDescription, productGroupDetailDescription).fetchJoin()
                        .leftJoin(productGroup.productScore, productScore).fetchJoin()
                        .join(product.productStock, productStock).fetchJoin()
                        .leftJoin(product.productOptions, productOption).fetchJoin()
                        .leftJoin(productOption.optionGroup, optionGroup).fetchJoin()
                        .leftJoin(productOption.optionDetail, optionDetail).fetchJoin()
                        .where(
                                productIdEq(productId), deleteYn()
                        )
                        .distinct()
                        .fetchOne()
        );
    }

    @Override
    public List<ProductFetchDto> fetchProducts(List<Long> productGroupIds){
        return queryFactory
                        .select(
                                new QProductFetchDto(
                                        product.productGroup.id,
                                        product.id,
                                        productStock.stockQuantity,
                                        product.productStatus,
                                        optionGroup.id,
                                        optionDetail.id,
                                        optionGroup.optionName,
                                        optionDetail.optionValue,
                                        productOption.additionalPrice.coalesce(BigDecimal.ZERO)
                                )
                        )
                        .from(product)
                        .innerJoin(product.productStock, productStock)
                        .leftJoin(product.productOptions, productOption)
                            .on(productOption.deleteYn.eq(Yn.N))
                        .leftJoin(productOption.optionGroup, optionGroup)
                        .leftJoin(productOption.optionDetail, optionDetail)
                            .on(optionDetail.deleteYn.eq(Yn.N))
                        .distinct()
                        .where(productGroupIdIn(productGroupIds), deleteYn())
                        .fetch();
    }

    @Override
    public List<ProductFetchDto> fetchProducts(Long productGroupId) {
        return queryFactory
            .select(
                new QProductFetchDto(
                    product.productGroup.id,
                    product.id,
                    productStock.stockQuantity,
                    product.productStatus,
                    optionGroup.id,
                    optionDetail.id,
                    optionGroup.optionName,
                    optionDetail.optionValue,
                    productOption.additionalPrice.coalesce(BigDecimal.ZERO)
                )
            )
            .from(product)
            .innerJoin(product.productStock, productStock)
            .leftJoin(product.productOptions, productOption)
            .on(productOption.deleteYn.eq(Yn.N))
            .leftJoin(productOption.optionGroup, optionGroup)
            .leftJoin(productOption.optionDetail, optionDetail)
            .on(optionDetail.deleteYn.eq(Yn.N))
            .distinct()
            .where(product.productGroup.id.eq(productGroupId))
            .fetch();
    }

    @Override
    public List<Product> fetchProductEntities(Long productGroupId, List<Long> productIds) {
        return queryFactory
                .selectFrom(product)
                .join(product.productGroup, productGroup).fetchJoin()
                .join(productGroup.productDelivery, productDelivery).fetchJoin()
                .join(productGroup.productNotice, productNotice).fetchJoin()
                .join(productGroup.detailDescription, productGroupDetailDescription).fetchJoin()
                .join(product.productStock, productStock).fetchJoin()
                .leftJoin(productGroup.productRatingStats, productRatingStats).fetchJoin()
                .leftJoin(productGroup.productScore, productScore).fetchJoin()
                .leftJoin(product.productOptions, productOption).fetchJoin()
                .leftJoin(productOption.optionGroup, optionGroup).fetchJoin()
                .leftJoin(productOption.optionDetail, optionDetail).fetchJoin()
                .where(
                        productGroupIdEq(productGroupId),
                        productIdIn(productIds),
                        deleteYn()
                )
                .distinct()
                .fetch();
    }

    private BooleanExpression productIdEq(long productId) {
        return product.id.eq(productId);
    }

    private BooleanExpression productGroupIdEq(Long productGroupId) {
        if(productGroupId != null) return product.productGroup.id.eq(productGroupId);
        return null;
    }

    private BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        return product.productGroup.id.in(productGroupIds);
    }

    private BooleanExpression productIdIn(List<Long> productIds){
        if(!productIds.isEmpty()) return product.id.in(productIds);
        return null;
    }

    private BooleanExpression deleteYn(){
        return product.deleteYn.eq(Yn.N);
    }



}
