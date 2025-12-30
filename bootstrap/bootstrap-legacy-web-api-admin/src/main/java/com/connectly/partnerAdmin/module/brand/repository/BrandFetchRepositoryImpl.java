package com.connectly.partnerAdmin.module.brand.repository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.core.ExternalBrandContext;
import com.connectly.partnerAdmin.module.brand.core.QExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.core.QExternalBrandContext;
import com.connectly.partnerAdmin.module.brand.filter.BrandFilter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.connectly.partnerAdmin.module.brand.entity.QBrand.brand;
import static com.connectly.partnerAdmin.module.brand.entity.QBrandMapping.brandMapping;


@Repository
@RequiredArgsConstructor
public class BrandFetchRepositoryImpl implements BrandFetchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public boolean hasBrandIdExist(long brandId) {
        Integer integer = queryFactory
                .selectOne()
                .from(brand)
                .where(brandIdEq(brandId))
                .fetchFirst();

        return integer != null;
    }

    @Override
    public List<ExtendedBrandContext> fetchBrandContexts(BrandFilter filter, Pageable pageable) {
        return queryFactory
                .select(
                        new QExtendedBrandContext(
                                brand.id,
                                brand.brandName,
                                brand.mainDisplayType,
                                brand.displayEnglishName,
                                brand.displayKoreanName
                        )
                ).from(brand)
                .where(isBrandNameLike(filter))
                .orderBy(brand.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<ExtendedBrandContext> fetchBrandContextsWithNoOffset(BrandFilter filter, Pageable pageable) {
        return queryFactory
                .select(
                        new QExtendedBrandContext(
                                brand.id,
                                brand.brandName,
                                brand.mainDisplayType,
                                brand.displayEnglishName,
                                brand.displayKoreanName
                        )
                ).from(brand)
                .where(getBrandIdCondition(filter, pageable), isBrandNameLike(filter))
                .orderBy(getOrderSpecifier(pageable))
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<ExternalBrandContext> fetchExternalBrandMappingContexts(long siteId, List<String> mappingBrandIds) {
        return queryFactory
                .select(
                        new QExternalBrandContext(
                                brand.id,
                                brand.brandName,
                                brandMapping.id,
                                brandMapping.mappingBrandId
                        )
                )
                .from(brand)
                .innerJoin(brandMapping).on(brandMapping.brandId.eq(brand.id))
                .where(mappingBrandIdIn(mappingBrandIds), siteIdEq(siteId))
                .fetch();
    }


    @Override
    public JPAQuery<Long> fetchBrandCountQuery(BrandFilter filter){
        return queryFactory
                .select(brand.count())
                .from(brand)
                .where(isBrandNameLike(filter))
                .distinct();
    }

    @Override
    public Optional<ExtendedBrandContext> fetchBrandWithNameLike(String brandName) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QExtendedBrandContext(
                                        brand.id,
                                        brand.brandName,
                                        brand.mainDisplayType,
                                        brand.displayEnglishName,
                                        brand.displayKoreanName
                                ))
                        .from(brand)
                        .where(brandNameLike(brandName))
                        .fetchOne()
        );
    }

    @Override
    public Optional<ExtendedBrandContext> fetchBrandWithNameEq(String brandName) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QExtendedBrandContext(
                                        brand.id,
                                        brand.brandName,
                                        brand.mainDisplayType,
                                        brand.displayEnglishName,
                                        brand.displayKoreanName
                                ))
                        .from(brand)
                        .where(brandNameEq(brandName))
                        .fetchOne()
        );
    }


    @Override
    public Long fetchBySiteIdAndMappingBrandId(long siteId, String mappingBrandId) {
        return queryFactory
            .select(
                brandMapping.brandId
            )
            .from(brandMapping)
            .where(brandMapping.mappingBrandId.eq(mappingBrandId), brandMapping.siteId.eq(siteId))
            .fetchOne();
    }


    private BooleanExpression brandIdEq(long brandId) {
        return brand.id.eq(brandId);
    }

    private BooleanExpression getBrandIdCondition(BrandFilter filter, Pageable pageable) {
        Sort.Order order = pageable.getSort().getOrderFor("id");
        if (order != null && order.isAscending()) {
            return isBrandIdGt(filter);
        } else {
            return isBrandIdLt(filter);
        }
    }


    private BooleanExpression isBrandIdLt(BrandFilter filter){
        if(filter.getLastDomainId() !=null) return brand.id.lt(filter.getLastDomainId());
        else return null;
    }

    private BooleanExpression isBrandIdGt(BrandFilter filter){
        if(filter.getLastDomainId() !=null) return brand.id.gt(filter.getLastDomainId());
        else return null;
    }

    private BooleanExpression isBrandNameLike(BrandFilter filter){
        if(StringUtils.hasText(filter.getBrandName())){
            if(filter.getMainDisplayType().isKorBrandName()) return brand.displayKoreanName.contains(filter.getBrandName());
            else return brand.displayEnglishName.contains(filter.getBrandName());
        }
        else return null;
    }

    private BooleanExpression brandNameLike(String brandName){
        if(brandName ==null || brandName.isBlank()) return null;
        return brand.brandName.like("%" + brandName + "%");
    }

    private BooleanExpression brandNameEq(String brandName){
        if(brandName ==null || brandName.isBlank()) return null;
        return brand.brandName.eq(brandName);
    }


    private BooleanExpression mappingBrandIdIn(List<String> mappingBrandIds){
        return brandMapping.mappingBrandId.in(mappingBrandIds);
    }

    private BooleanExpression siteIdEq(long siteId){
        return brandMapping.siteId.eq(siteId);
    }


    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
        Sort.Order order = pageable.getSort().getOrderFor("id");
        if (order != null && order.isAscending()) {
            return brand.id.asc();
        } else {
            return brand.id.desc();
        }
    }

}
