package com.connectly.partnerAdmin.module.product.repository.group;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.connectly.partnerAdmin.module.brand.core.QBaseBrandContext;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.product.core.ExternalContext;
import com.connectly.partnerAdmin.module.product.core.QExternalContext;
import com.connectly.partnerAdmin.module.product.core.QProductGroupInfo;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.QProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.QProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.crawl.QCrawlProductDto;
import com.connectly.partnerAdmin.module.product.dto.external.QExternalProductDto;
import com.connectly.partnerAdmin.module.product.dto.image.QProductImageDto;
import com.connectly.partnerAdmin.module.product.dto.notice.QProductNoticeDto;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.connectly.partnerAdmin.module.brand.entity.QBrand.brand;
import static com.connectly.partnerAdmin.module.category.entity.QCategory.category;
import static com.connectly.partnerAdmin.module.crawl.entity.QCrawlProduct.crawlProduct;
import static com.connectly.partnerAdmin.module.external.entity.QExternalProduct.externalProduct;
import static com.connectly.partnerAdmin.module.product.entity.delivery.QProductDelivery.productDelivery;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupDetailDescription.productGroupDetailDescription;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.connectly.partnerAdmin.module.product.entity.notice.QProductNotice.productNotice;
import static com.connectly.partnerAdmin.module.product.entity.score.QProductRatingStats.productRatingStats;
import static com.connectly.partnerAdmin.module.product.entity.score.QProductScore.productScore;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;


@Repository
public class ProductGroupFetchRepositoryImpl extends AbstractCommonRepository implements ProductGroupFetchRepository {

    protected ProductGroupFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public Optional<ProductGroupFetchResponse> fetchProductGroup(long productGroupId, Optional<Long> sellerId) {
        return Optional.ofNullable(
                getQueryFactory()
                        .from(productGroup)
                        .leftJoin(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))
                        .leftJoin(seller).on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                        .leftJoin(productGroup.productNotice, productNotice)
                        .leftJoin(productGroup.productDelivery, productDelivery)
                        .leftJoin(productGroup.images, productGroupImage)
                            .on(productGroupImage.deleteYn.eq(Yn.N))
                        .innerJoin(productGroup.detailDescription, productGroupDetailDescription)
                        .leftJoin(crawlProduct).on(crawlProduct.productGroupId.eq(productGroup.id))
                        .leftJoin(externalProduct).on(externalProduct.productGroupId.eq(productGroup.id))
                        .distinct()
                        .where(productGroupIdEq(productGroupId), sellerIdEq(sellerId))
                        .transform(GroupBy.groupBy(productGroup.id).as(
                                new QProductGroupFetchResponse(
                                        new QProductGroupInfo(
                                                productGroup.id,
                                                productGroup.productGroupDetails.productGroupName,
                                                seller.id,
                                                seller.sellerName,
                                                productGroup.productGroupDetails.categoryId,
                                                productGroup.productGroupDetails.optionType,
                                                productGroup.productGroupDetails.managementType,
                                                new QBaseBrandContext(
                                                        brand.id,
                                                        brand.brandName
                                                ),
                                                productGroup.productGroupDetails.price,
                                                productGroup.productGroupDetails.clothesDetailInfo,
                                                productDelivery.deliveryNotice,
                                                productDelivery.refundNotice,
                                                productGroup.productGroupDetails.productStatus,
                                                productGroup.insertDate,
                                                productGroup.updateDate,
                                                productGroup.insertOperator,
                                                productGroup.updateOperator,
                                                crawlProduct.crawlProductSku,
                                                new QCrawlProductDto(
                                                        crawlProduct.siteId.coalesce(0L),
                                                        crawlProduct.crawlProductSku.coalesce(0L),
                                                        crawlProduct.updateStatus,
                                                        crawlProduct.insertDate,
                                                        crawlProduct.updateDate
                                                ),
                                                productGroup.productGroupDetails.externalProductUuId.coalesce(""),
                                                GroupBy.set(
                                                        new QExternalProductDto(
                                                                externalProduct.siteId.coalesce(0L),
                                                                externalProduct.externalIdx.coalesce(""),
                                                                externalProduct.mappingStatus,
                                                                externalProduct.insertDate,
                                                                externalProduct.updateDate
                                                        )
                                                )
                                        ),
                                        new QProductNoticeDto(
                                                productNotice.noticeDetail.material,
                                                productNotice.noticeDetail.color,
                                                productNotice.noticeDetail.size,
                                                productNotice.noticeDetail.maker,
                                                productNotice.noticeDetail.origin,
                                                productNotice.noticeDetail.washingMethod,
                                                productNotice.noticeDetail.yearMonth,
                                                productNotice.noticeDetail.assuranceStandard,
                                                productNotice.noticeDetail.asPhone
                                        ),
                                        GroupBy.set(
                                                new QProductImageDto(
                                                        productGroupImage.imageDetail.productGroupImageType,
                                                        productGroupImage.imageDetail.imageUrl
                                                )
                                        ),
                                        productGroupDetailDescription.imageDetail.imageUrl
                                ))).get(productGroupId));
    }

    @Override
    public List<ProductGroupFetchResponse> fetchProductGroup(String externalProductUuId) {
        return
            getQueryFactory()
                .from(productGroup)
                .leftJoin(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .leftJoin(seller).on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                .leftJoin(productGroup.productNotice, productNotice)
                .leftJoin(productGroup.productDelivery, productDelivery)
                .leftJoin(productGroup.images, productGroupImage)
                .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(productGroup.detailDescription, productGroupDetailDescription)
                .leftJoin(crawlProduct).on(crawlProduct.productGroupId.eq(productGroup.id))
                .leftJoin(externalProduct).on(externalProduct.productGroupId.eq(productGroup.id))
                .where(productGroup.productGroupDetails.externalProductUuId.eq(externalProductUuId))
                .transform(GroupBy.groupBy(productGroup.id).list(
                    new QProductGroupFetchResponse(
                        new QProductGroupInfo(
                            productGroup.id,
                            productGroup.productGroupDetails.productGroupName,
                            seller.id,
                            seller.sellerName,
                            productGroup.productGroupDetails.categoryId,
                            productGroup.productGroupDetails.optionType,
                            productGroup.productGroupDetails.managementType,
                            new QBaseBrandContext(
                                brand.id,
                                brand.brandName
                            ),
                            productGroup.productGroupDetails.price,
                            productGroup.productGroupDetails.clothesDetailInfo,
                            productDelivery.deliveryNotice,
                            productDelivery.refundNotice,
                            productGroup.productGroupDetails.productStatus,
                            productGroup.insertDate,
                            productGroup.updateDate,
                            productGroup.insertOperator,
                            productGroup.updateOperator,
                            crawlProduct.crawlProductSku,
                            new QCrawlProductDto(
                                crawlProduct.siteId.coalesce(0L),
                                crawlProduct.crawlProductSku.coalesce(0L),
                                crawlProduct.updateStatus,
                                crawlProduct.insertDate,
                                crawlProduct.updateDate
                            ),
                            productGroup.productGroupDetails.externalProductUuId.coalesce(""),
                            GroupBy.set(
                                new QExternalProductDto(
                                    externalProduct.siteId.coalesce(0L),
                                    externalProduct.externalIdx.coalesce(""),
                                    externalProduct.mappingStatus,
                                    externalProduct.insertDate,
                                    externalProduct.updateDate
                                )
                            )
                        ),
                        new QProductNoticeDto(
                            productNotice.noticeDetail.material,
                            productNotice.noticeDetail.color,
                            productNotice.noticeDetail.size,
                            productNotice.noticeDetail.maker,
                            productNotice.noticeDetail.origin,
                            productNotice.noticeDetail.washingMethod,
                            productNotice.noticeDetail.yearMonth,
                            productNotice.noticeDetail.assuranceStandard,
                            productNotice.noticeDetail.asPhone
                        ),
                        GroupBy.set(
                            new QProductImageDto(
                                productGroupImage.imageDetail.productGroupImageType,
                                productGroupImage.imageDetail.imageUrl
                            )
                        ),
                        productGroupDetailDescription.imageDetail.imageUrl
                    )));
    }

    @Override
    public List<Long> fetchProductGroupIds() {
        return getQueryFactory()
            .select(productGroup.id)
            .from(productGroup)
            .where(productGroup.id.gt(514462L))
            .fetch();
    }

    @Override
    public JPAQuery<Long> fetchProductGroupCountQuery(ProductGroupFilter filter){
        return getQueryFactory()
                .select(productGroup.count())
                .from(productGroup)
                .innerJoin(seller).on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                .where(
                        betweenTime(filter), managementTypeEq(filter), brandEq(filter),
                        sellerIdEq(Optional.ofNullable(filter.getSellerId())), soldOutEq(filter), displayEq(filter), categoryIn(filter),
                        betweenPrice(filter), betweenSalePercent(filter),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()), deleteYn()
                )
                .distinct();
    }

    @Override
    public List<ProductGroupDetailResponse> fetchProductGroups(ProductGroupFilter filter, Pageable pageable) {
        List<Long> productGroupIds = fetchProductGroupIds(filter, pageable);
        return fetchProductGroups(productGroupIds, pageable);
    }

    @Override
    public List<ProductGroupDetailResponse> fetchProductsWithNoOffset(ProductGroupFilter filter, Pageable pageable) {
        List<Long> productGroupIds = fetchProductGroupIdsWithNoOffset(filter, pageable);
        return fetchProductGroups(productGroupIds, pageable);
    }

    private List<Long> fetchProductGroupIds(ProductGroupFilter filter, Pageable pageable){
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable, productGroup);

        return getQueryFactory()
                .select(productGroup.id)
                .from(productGroup)
                .where(
                        betweenTime(filter), managementTypeEq(filter), brandEq(filter),
                        sellerIdEq(Optional.ofNullable(filter.getSellerId())), soldOutEq(filter), displayEq(filter), categoryIn(filter),
                        betweenPrice(filter), betweenSalePercent(filter),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()), deleteYn()
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private List<Long> fetchProductGroupIdsWithNoOffset(Long offset, int size, List<Long> sellerIds){

        return getQueryFactory()
                .select(productGroup.id)
                .from(productGroup)
                .where(
                       lt(offset), deleteYn(), sellerIdIn(sellerIds)
                )
                .orderBy(productGroup.id.desc())
                .limit(size)
                .fetch();
    }

    private List<Long> fetchProductGroupIdsWithNoOffsetAsc(Long offset, int size, List<Long> sellerIds){

        return getQueryFactory()
            .select(productGroup.id)
            .from(productGroup)
            .where(
                gt(offset), deleteYn(), sellerIdIn(sellerIds)
            )
            .orderBy(productGroup.id.asc())
            .limit(size)
            .fetch();
    }

    private List<Long> fetchProductGroupIdsWithNoOffset(ProductGroupFilter filter, Pageable pageable){
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable, productGroup);
        BooleanExpression noOffsetCondition = getNoOffsetCondition(filter, orders);

        return getQueryFactory()
            .select(productGroup.id)
            .from(productGroup)
            .where(
                betweenTime(filter), managementTypeEq(filter), brandEq(filter),
                sellerIdEq(Optional.ofNullable(filter.getSellerId())), soldOutEq(filter), displayEq(filter), categoryIn(filter),
                betweenPrice(filter), betweenSalePercent(filter),
                searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()), deleteYn(),
                noOffsetCondition
            )
            .orderBy(orders.toArray(OrderSpecifier[]::new))
            .limit(pageable.getPageSize())
            .fetch();
    }

    private List<ProductGroupDetailResponse> fetchProductGroups(List<Long> productGroupIds, Pageable pageable) {
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable, productGroup);
        return getQueryFactory()
                .from(productGroup)
                .innerJoin(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .innerJoin(category).on(category.id.eq(productGroup.productGroupDetails.categoryId))
                .innerJoin(seller).on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                .innerJoin(productGroup.productDelivery, productDelivery)
                .innerJoin(productGroup.images, productGroupImage)
                    .on(productGroupImage.deleteYn.eq(Yn.N))
                    .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                .leftJoin(crawlProduct).on(crawlProduct.productGroupId.eq(productGroup.id))
                .leftJoin(externalProduct).on(externalProduct.productGroupId.eq(productGroup.id))
                .distinct()
                .where(productGroupIdIn(productGroupIds))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .transform(GroupBy.groupBy(productGroup.id).list(
                        new QProductGroupDetailResponse(
                                new QProductGroupInfo(
                                        productGroup.id,
                                        productGroup.productGroupDetails.productGroupName,
                                        seller.id,
                                        seller.sellerName,
                                        productGroup.productGroupDetails.categoryId,
                                        productGroup.productGroupDetails.optionType,
                                        productGroup.productGroupDetails.managementType,
                                        new QBaseBrandContext(
                                                brand.id,
                                                brand.brandName
                                        ),
                                        productGroup.productGroupDetails.price,
                                        productGroup.productGroupDetails.clothesDetailInfo,
                                        productDelivery.deliveryNotice,
                                        productDelivery.refundNotice,
                                        productGroupImage.imageDetail.imageUrl,
                                        category.path,
                                        productGroup.productGroupDetails.productStatus,
                                        productGroup.insertDate,
                                        productGroup.updateDate,
                                        productGroup.insertOperator,
                                        productGroup.updateOperator,
                                        crawlProduct.crawlProductSku,
                                        new QCrawlProductDto(
                                                crawlProduct.siteId.coalesce(0L),
                                                crawlProduct.crawlProductSku.coalesce(0L),
                                                crawlProduct.updateStatus,
                                                crawlProduct.insertDate,
                                                crawlProduct.updateDate
                                        ),
                                        GroupBy.set(
                                                new QExternalProductDto(
                                                        externalProduct.siteId.coalesce(0L),
                                                        externalProduct.externalIdx.coalesce(""),
                                                        externalProduct.mappingStatus,
                                                        externalProduct.insertDate,
                                                        externalProduct.updateDate
                                                )
                                        )

                                )
                        )));
    }



    @Override
    public Optional<ProductGroup> fetchProductGroupEntity(long productGroupId) {
        return Optional.ofNullable(
                getQueryFactory().select(productGroup)
                        .from(productGroup)
                        .join(productGroup.productDelivery, productDelivery).fetchJoin()
                        .join(productGroup.productNotice, productNotice).fetchJoin()
                        .join(productGroup.images, productGroupImage).fetchJoin()
                        .join(productGroup.detailDescription, productGroupDetailDescription).fetchJoin()
                        .leftJoin(productGroup.productScore, productScore).fetchJoin()
                        .leftJoin(productGroup.productRatingStats, productRatingStats).fetchJoin()
                        .where(productGroupIdEq(productGroupId), imageNotDelete())
                        .fetchOne()
        );
    }


    public List<ExternalContext> fetchExternalProducts(long siteId){
        return getQueryFactory().select(
            new QExternalContext(
                externalProduct.productGroupId,
                externalProduct.siteId,
                externalProduct.externalIdx
            )
            )
            .from(externalProduct)
            .where(
                externalProduct.siteId.eq(siteId),
                externalProduct.externalIdx.isNotNull()
            ).fetch();

    }


    @Override
    public List<ProductGroupDetailResponse> fetchProductGroups(List<Long> productGroupIds) {
        PageRequest pageRequest = PageRequest.of(0, productGroupIds.size());
        return fetchProductGroups(productGroupIds, pageRequest);
    }

    @Override
    public List<ProductGroupDetailResponse> fetchProductsWithNoOffset(Long offset, int size, List<Long> sellerIds) {
        List<Long> productGroupIds = fetchProductGroupIdsWithNoOffset(offset, size, sellerIds);
        if(productGroupIds.isEmpty()) return List.of();
        return fetchProductGroups(productGroupIds);
    }

    @Override
    public List<ProductGroupDetailResponse> fetchProductsWithNoOffsetAsc(Long offset, int size, List<Long> sellerIds) {
        List<Long> productGroupIds = fetchProductGroupIdsWithNoOffsetAsc(offset, size, sellerIds);
        if(productGroupIds.isEmpty()) return List.of();
        return fetchProductGroups(productGroupIds);
    }

    private BooleanExpression productGroupIdIn(List<Long> productGroupIds){
        return productGroup.id.in(productGroupIds);
    }

    private BooleanExpression betweenTime(ProductGroupFilter dto){
        return productGroup.insertDate.between(dto.getStartDate(), dto.getEndDate());
    }

    private BooleanExpression managementTypeEq(ProductGroupFilter dto) {
        return dto.getManagementType() !=null ? productGroup.productGroupDetails.managementType.eq(dto.getManagementType()) : null;
    }

    private BooleanExpression brandEq(ProductGroupFilter dto) {
        return dto.getBrandId() != null ? productGroup.productGroupDetails.brandId.eq(dto.getBrandId()) : null;
    }


    private BooleanExpression sellerIdEq(Optional<Long> sellerId){
        return sellerId.map(productGroup.productGroupDetails.sellerId::eq).orElse(null);
    }

    private BooleanExpression sellerIdIn(List<Long> sellerId){
        if(sellerId == null){
            return null;
        };
        return productGroup.productGroupDetails.sellerId.in(sellerId);
    }

    private BooleanExpression soldOutEq(ProductGroupFilter dto) {
        return dto.getSoldOutYn() !=null ? productGroup.productGroupDetails.productStatus.soldOutYn.eq(dto.getSoldOutYn()) : null;
    }

    private BooleanExpression displayEq(ProductGroupFilter dto) {
        return dto.getDisplayYn() !=null ? productGroup.productGroupDetails.productStatus.displayYn.eq(dto.getDisplayYn()) : null;
    }

    private BooleanExpression deleteYn() {
        return  productGroup.deleteYn.eq(Yn.N);
    }

    private BooleanExpression imageNotDelete(){
        return productGroupImage.deleteYn.eq(Yn.N);
    }

    private BooleanExpression betweenPrice(ProductGroupFilter dto) {
        if (dto.getMinSalePrice() != null && dto.getMaxSalePrice() != null && dto.getMinSalePrice() >= 0 && dto.getMaxSalePrice() > 0) {
            BigDecimal minPrice = BigDecimal.valueOf(dto.getMinSalePrice());
            BigDecimal maxPrice = BigDecimal.valueOf(dto.getMaxSalePrice());
            return productGroup.productGroupDetails.price.salePrice.between(minPrice, maxPrice);
        }
        return null;
    }

    private BooleanExpression betweenSalePercent(ProductGroupFilter dto) {
        if(dto.getMinDiscountRate() != null && dto.getMaxDiscountRate() != null && dto.getMinDiscountRate() >=0 && dto.getMaxDiscountRate() >0){
            return productGroup.productGroupDetails.price.discountRate.between(dto.getMinDiscountRate(), dto.getMaxDiscountRate());
        }
        return null;
    }

    private BooleanExpression productGroupIdEq(long productGroupId) {
        return productGroupId > 0 ? productGroup.id.eq(productGroupId) : null;
    }


    @Override
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable, Path<?> path) {
        List<OrderSpecifier<?>> allOrderSpecifiers = super.getAllOrderSpecifiers(pageable, path);
        if (allOrderSpecifiers.isEmpty()) {
            allOrderSpecifiers.add(productGroup.id.desc());
        }
        return allOrderSpecifiers;
    }

    private BooleanExpression getNoOffsetCondition(ProductGroupFilter filter, List<OrderSpecifier<?>> orders) {
        if (filter.getLastDomainId() == null || orders.isEmpty()) {
            return null;
        }

        for (OrderSpecifier<?> order : orders) {
            if (order.getTarget().equals(productGroup.id)) {
                if (order.getOrder() == Order.ASC) {
                    return isProductGroupIdGt(filter);
                } else if (order.getOrder() == Order.DESC) {
                    return isProductGroupIdLt(filter);
                }
            }
        }

        return null;
    }

    private BooleanExpression isProductGroupIdLt(ProductGroupFilter filter){
        if(filter.getLastDomainId() !=null) return productGroup.id.lt(filter.getLastDomainId());
        else return null;
    }

    private BooleanExpression isProductGroupIdGt(ProductGroupFilter filter){
        if(filter.getLastDomainId() !=null) return productGroup.id.gt(filter.getLastDomainId());
        else return null;
    }

    private BooleanExpression gt(Long offset) {
        if(offset == null) return null;
        else return productGroup.id.gt(offset);
    }

    private BooleanExpression lt(Long offset) {
        if(offset == null) return null;
        else return productGroup.id.lt(offset);
    }

    private BooleanExpression categoryIn(ProductGroupFilter filter) {

        if(filter.getCategoryIds() != null){
            if(!filter.getCategoryIds().isEmpty()){
                return productGroup.productGroupDetails.categoryId.in(filter.getCategoryIds());
            }
            return null;
        }
        return null;
    }

}
