package com.setof.connectly.module.product.repository.group;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.category.entity.QCategory.category;
import static com.setof.connectly.module.product.entity.delivery.QProductDelivery.productDelivery;
import static com.setof.connectly.module.product.entity.group.QProduct.product;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupDetailDescription.productGroupDetailDescription;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.product.entity.notice.QProductNotice.productNotice;
import static com.setof.connectly.module.product.entity.option.QOptionDetail.optionDetail;
import static com.setof.connectly.module.product.entity.option.QOptionGroup.optionGroup;
import static com.setof.connectly.module.product.entity.option.QProductOption.productOption;
import static com.setof.connectly.module.product.entity.stock.QProductStock.productStock;
import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;
import static com.setof.connectly.module.search.entity.QProductScore.productScore;
import static com.setof.connectly.module.seller.entity.QSeller.seller;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.dto.CursorDto;
import com.setof.connectly.module.common.dto.QCursorDto;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.common.filter.ItemFilter;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.repository.component.AbstractComponentRepository;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.QProductGroupThumbnail;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.dto.delivery.RefundNoticeDto;
import com.setof.connectly.module.product.dto.filter.ProductFilter;
import com.setof.connectly.module.product.dto.group.ClothesDetailDto;
import com.setof.connectly.module.product.dto.group.fetch.ProductFetchDto;
import com.setof.connectly.module.product.dto.group.fetch.ProductGroupFetchDto;
import com.setof.connectly.module.product.dto.group.fetch.ProductFetchDto;
import com.setof.connectly.module.product.dto.group.fetch.QProductFetchDto;
import com.setof.connectly.module.product.dto.image.ProductImageDto;
import com.setof.connectly.module.product.dto.image.QProductImageDto;
import com.setof.connectly.module.product.dto.notice.ProductNoticeDto;
import com.setof.connectly.module.product.dto.price.ProductGroupPriceDto;
import com.setof.connectly.module.product.dto.price.QProductGroupPriceDto;
import com.setof.connectly.module.product.dto.review.ProductReviewDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.delivery.RefundNoticeDto;
import com.setof.connectly.module.product.dto.group.ClothesDetailDto;
import com.setof.connectly.module.product.dto.image.ProductImageDto;
import com.setof.connectly.module.product.dto.notice.ProductNoticeDto;
import com.setof.connectly.module.product.dto.review.ProductReviewDto;
import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import com.setof.connectly.module.product.enums.option.OptionType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductGroupFindRepositoryImpl extends AbstractComponentRepository
        implements ProductGroupFindRepository {

    public ProductGroupFindRepositoryImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public Optional<ProductGroupFetchDto> fetchProductGroupDto(long productGroupId) {
        // 1. 기본 정보 쿼리 (1:1 관계 - 6개 테이블)
        ProductGroupFetchDto basicInfo = fetchBasicInfo(productGroupId);
        if (basicInfo == null) {
            return Optional.empty();
        }

        // 2. 상품 + 옵션 쿼리 (1:N 관계 - 5개 테이블)
        Set<ProductFetchDto> products = fetchProducts(productGroupId);
        basicInfo.setProducts(products);

        // 3. 이미지 쿼리 (1:N 관계 - 1개 테이블)
        Set<ProductImageDto> images = fetchImages(productGroupId);
        basicInfo.setProductImages(images);

        return Optional.of(basicInfo);
    }

    /**
     * 기본 정보 쿼리: productGroup, seller, brand, category, productDelivery, productNotice,
     * productGroupDetailDescription, productRatingStats (1:1 관계만)
     */
    private ProductGroupFetchDto fetchBasicInfo(long productGroupId) {
        var tuple = queryFactory
                .select(
                        productGroup.id,
                        productGroup.productGroupDetails.productGroupName,
                        seller.id,
                        seller.sellerName,
                        brand.id,
                        brand.brandName,
                        category.id,
                        category.path,
                        productGroup.productGroupDetails.price,
                        productGroup.productGroupDetails.optionType,
                        productGroup.productGroupDetails.clothesDetailInfo.productCondition,
                        productGroup.productGroupDetails.clothesDetailInfo.origin,
                        productGroup.productGroupDetails.productStatus,
                        productDelivery.deliveryNotice,
                        productDelivery.refundNotice.returnMethodDomestic,
                        productDelivery.refundNotice.returnCourierDomestic,
                        productDelivery.refundNotice.returnChargeDomestic,
                        productDelivery.refundNotice.returnExchangeAreaDomestic,
                        productRatingStats.averageRating.coalesce(0.0),
                        productRatingStats.reviewCount.coalesce(0L),
                        productGroupDetailDescription.imageDetail.imageUrl,
                        productNotice.id,
                        productNotice.noticeDetail.material,
                        productNotice.noticeDetail.color,
                        productNotice.noticeDetail.size,
                        productNotice.noticeDetail.maker,
                        productNotice.noticeDetail.origin,
                        productNotice.noticeDetail.washingMethod,
                        productNotice.noticeDetail.yearMonth,
                        productNotice.noticeDetail.assuranceStandard,
                        productNotice.noticeDetail.asPhone)
                .from(productGroup)
                .innerJoin(seller).on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                .innerJoin(category).on(category.id.eq(productGroup.productGroupDetails.categoryId))
                .innerJoin(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .innerJoin(productDelivery).on(productDelivery.productGroup.id.eq(productGroup.id))
                .innerJoin(productNotice).on(productNotice.productGroup.id.eq(productGroup.id))
                .innerJoin(productGroupDetailDescription)
                    .on(productGroupDetailDescription.id.eq(productGroup.id))
                    .on(productGroupDetailDescription.deleteYn.eq(Yn.N))
                .leftJoin(productRatingStats).on(productRatingStats.id.eq(productGroup.id))
                .where(productGroupIdEq(productGroupId))
                .fetchOne();

        if (tuple == null) {
            return null;
        }

        return new ProductGroupFetchDto(
                tuple.get(productGroup.id),
                tuple.get(productGroup.productGroupDetails.productGroupName),
                tuple.get(seller.id),
                tuple.get(seller.sellerName),
                new BrandDto(tuple.get(brand.id), tuple.get(brand.brandName)),
                tuple.get(category.id),
                tuple.get(category.path),
                tuple.get(productGroup.productGroupDetails.price),
                tuple.get(productGroup.productGroupDetails.optionType),
                new ClothesDetailDto(
                        tuple.get(productGroup.productGroupDetails.clothesDetailInfo.productCondition),
                        tuple.get(productGroup.productGroupDetails.clothesDetailInfo.origin)),
                tuple.get(productGroup.productGroupDetails.productStatus),
                tuple.get(productDelivery.deliveryNotice),
                new RefundNoticeDto(
                        tuple.get(productDelivery.refundNotice.returnMethodDomestic),
                        tuple.get(productDelivery.refundNotice.returnCourierDomestic),
                        tuple.get(productDelivery.refundNotice.returnChargeDomestic),
                        tuple.get(productDelivery.refundNotice.returnExchangeAreaDomestic)),
                new ProductReviewDto(
                        tuple.get(productRatingStats.averageRating.coalesce(0.0)),
                        tuple.get(productRatingStats.reviewCount.coalesce(0L))),
                tuple.get(productGroupDetailDescription.imageDetail.imageUrl),
                new ProductNoticeDto(
                        tuple.get(productNotice.id),
                        tuple.get(productNotice.noticeDetail.material),
                        tuple.get(productNotice.noticeDetail.color),
                        tuple.get(productNotice.noticeDetail.size),
                        tuple.get(productNotice.noticeDetail.maker),
                        tuple.get(productNotice.noticeDetail.origin),
                        tuple.get(productNotice.noticeDetail.washingMethod),
                        tuple.get(productNotice.noticeDetail.yearMonth),
                        tuple.get(productNotice.noticeDetail.assuranceStandard),
                        tuple.get(productNotice.noticeDetail.asPhone)));
    }

    /**
     * 상품 목록 쿼리: product, productStock, productOption, optionGroup, optionDetail (1:N 관계)
     */
    private Set<ProductFetchDto> fetchProducts(long productGroupId) {
        return new HashSet<>(queryFactory
                .from(product)
                .innerJoin(productStock).on(productStock.product.id.eq(product.id))
                    .on(productStock.deleteYn.eq(Yn.N))
                .leftJoin(productOption).on(productOption.product.id.eq(product.id))
                    .on(productOption.deleteYn.eq(Yn.N))
                .leftJoin(optionGroup).on(optionGroup.id.eq(productOption.optionGroup.id))
                    .on(optionGroup.deleteYn.eq(Yn.N))
                .leftJoin(optionDetail).on(optionDetail.id.eq(productOption.optionDetail.id))
                    .on(optionDetail.deleteYn.eq(Yn.N))
                .where(
                        product.productGroup.id.eq(productGroupId),
                        product.deleteYn.eq(Yn.N))
                .transform(
                        GroupBy.groupBy(product.id, optionDetail.id)
                                .list(new QProductFetchDto(
                                        product.productGroup.id,
                                        product.id,
                                        productStock.stockQuantity,
                                        productOption.additionalPrice.coalesce(0L),
                                        product.productStatus,
                                        optionGroup.id.coalesce(0L),
                                        optionDetail.id.coalesce(0L),
                                        optionGroup.optionName,
                                        optionDetail.optionValue.coalesce("")))));
    }

    /**
     * 이미지 목록 쿼리: productGroupImage (1:N 관계)
     */
    private Set<ProductImageDto> fetchImages(long productGroupId) {
        return new HashSet<>(queryFactory
                .select(new QProductImageDto(
                        productGroupImage.id,
                        productGroupImage.imageDetail.productGroupImageType,
                        productGroupImage.imageDetail.imageUrl))
                .from(productGroupImage)
                .where(
                        productGroupImage.productGroup.id.eq(productGroupId),
                        productGroupImage.deleteYn.eq(Yn.N))
                .fetch());
    }

    @Override
    public Optional<ProductGroupPriceDto> fetchProductGroupPrice(long productId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QProductGroupPriceDto(
                                        productGroup.id,
                                        product.id,
                                        productGroup.productGroupDetails.price,
                                        productGroup.productGroupDetails.sellerId))
                        .from(product)
                        .innerJoin(productGroup)
                        .on(productGroup.id.eq(product.productGroup.id))
                        .where(productIdEq(productId))
                        .fetchOne());
    }

    @Override
    public List<ProductGroupPriceDto> fetchProductGroupPrices(List<Long> productIds) {
        return queryFactory
                .from(product)
                .innerJoin(productGroup)
                .on(productGroup.id.eq(product.productGroup.id))
                .where(productIdIn(productIds))
                .transform(
                        GroupBy.groupBy(product.id)
                                .list(
                                        new QProductGroupPriceDto(
                                                productGroup.id,
                                                product.id,
                                                productGroup.productGroupDetails.price,
                                                productGroup.productGroupDetails.sellerId)));
    }

    @Override
    public Optional<CursorDto> fetchLastProductGroupId(
            ComponentFilter filter, Set<Long> exclusiveProductIds) {
        filter.setLastDomainId(null);
        filter.setCursorValue(null);
        List<OrderSpecifier<?>> orders =
                createOrderSpecifiersFromPageable(productGroup, filter.getOrderType());
        BooleanExpression dynamicWhere = createDynamicWhereCondition(filter, filter.getOrderType());

        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QCursorDto(
                                        productGroup.id,
                                        productGroup.insertDate,
                                        productGroup.productGroupDetails.price.salePrice,
                                        productGroup.productGroupDetails.price.discountRate,
                                        productRatingStats.averageRating.coalesce(0.0),
                                        productRatingStats.reviewCount.coalesce(0L),
                                        productScore.score.coalesce(0.0)))
                        .from(productGroup)
                        .leftJoin(productRatingStats)
                        .on(productRatingStats.id.eq(productGroup.id))
                        .leftJoin(productScore)
                        .on(productScore.id.eq(productGroup.id))
                        .orderBy(orders.toArray(OrderSpecifier[]::new))
                        .where(
                                dynamicWhere,
                                productGroupIdNotIn(exclusiveProductIds),
                                brandIdIn(filter),
                                categoryIdIn(filter),
                                onSaleProduct())
                        .fetchFirst());
    }

    @Override
    public JPAQuery<Long> fetchProductCountQuery(ComponentFilter filter) {
        return queryFactory
                .select(productGroup.count())
                .from(productGroup)
                .where(
                        brandIdIn(filter),
                        categoryIdIn(filter),
                        onSaleProduct(),
                        betweenPrice(filter))
                .distinct();
    }

    @Override
    public List<ProductGroupThumbnail> fetchProductsWithSeller(long sellerId, Pageable pageable) {
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
                .where(sellerIdEq(sellerId))
                .orderBy(productScore.score.coalesce(0.0).desc())
                .limit(pageable.getPageSize())
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

    @Override
    public List<ProductGroupThumbnail> fetchProductsWithBrand(long brandId, Pageable pageable) {
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
                .where(brandIdEq(brandId))
                .orderBy(productScore.score.coalesce(0.0).desc())
                .limit(pageable.getPageSize())
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

    @Override
    public List<ProductGroupThumbnail> fetchProductGroups(ProductFilter filter, int pageSize) {
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
                        dynamicWhere,
                        onSaleProduct(),
                        sellerIdEq(filter.getSellerId()),
                        categoryIdIn(filter),
                        brandIdEq(filter.getBrandId()),
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

    @Override
    public List<ProductGroupThumbnail> fetchProductGroupsRecent(List<Long> productGroupIds) {
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
                .where(productGroupIdIn(productGroupIds), onSaleProduct())
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

    @Override
    public JPAQuery<Long> fetchProductCountQuery(ProductFilter filter) {
        return queryFactory
                .select(productGroup.count())
                .from(productGroup)
                .where(
                        sellerIdEq(filter.getSellerId()),
                        categoryIdIn(filter),
                        brandIdEq(filter.getBrandId()),
                        betweenPrice(filter),
                        onSaleProduct(),
                        betweenPrice(filter))
                .distinct();
    }

    @Override
    public List<Long> fetchProductGroupIds(List<Long> productIds) {
        return queryFactory
                .select(productGroup.id)
                .from(product)
                .innerJoin(productGroup)
                .on(productGroup.id.eq(product.productGroup.id))
                .where(productIdIn(productIds))
                .distinct()
                .fetch();
    }

    private BooleanExpression productGroupIdEq(long productGroupId) {
        return productGroupId > 0 ? productGroup.id.eq(productGroupId) : null;
    }

    private BooleanExpression productIdEq(long productId) {
        return product.id.eq(productId);
    }

    private BooleanExpression productIdIn(List<Long> productIds) {
        return product.id.in(productIds);
    }

    private BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? productGroup.productGroupDetails.sellerId.eq(sellerId) : null;
    }

    @Override
    protected BooleanExpression brandIdEq(Long brandId) {
        return brandId != null ? productGroup.productGroupDetails.brandId.eq(brandId) : null;
    }

    protected BooleanExpression betweenPrice(ItemFilter filterDto) {
        if (filterDto.getHighestPrice() != null || filterDto.getLowestPrice() != null) {
            Long highestPrice = filterDto.getHighestPrice();
            Long lowestPrice = filterDto.getLowestPrice();

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

    protected BooleanExpression categoryIdIn(ProductFilter filter) {
        List<Long> categoryIds = filter.getCategoryIds();
        if (categoryIds.isEmpty()) return null;
        return productGroup.productGroupDetails.categoryId.in(categoryIds);
    }

    protected BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        if (!productGroupIds.isEmpty()) return productGroup.id.in(productGroupIds);
        return null;
    }
}
