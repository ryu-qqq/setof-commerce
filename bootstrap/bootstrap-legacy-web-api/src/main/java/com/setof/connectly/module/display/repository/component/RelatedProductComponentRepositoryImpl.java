package com.setof.connectly.module.display.repository.component;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.display.entity.component.QComponent.component;
import static com.setof.connectly.module.display.entity.component.QComponentItem.componentItem;
import static com.setof.connectly.module.display.entity.component.QComponentTarget.componentTarget;
import static com.setof.connectly.module.display.entity.component.item.QBrandComponentItem.brandComponentItem;
import static com.setof.connectly.module.display.entity.component.item.QTab.tab;
import static com.setof.connectly.module.display.entity.component.sub.product.QBrandComponent.brandComponent;
import static com.setof.connectly.module.display.entity.component.sub.product.QCategoryComponent.categoryComponent;
import static com.setof.connectly.module.display.entity.component.sub.product.QProductComponent.productComponent;
import static com.setof.connectly.module.display.entity.component.sub.product.QTabComponent.tabComponent;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;
import static com.setof.connectly.module.search.entity.QProductScore.productScore;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.component.ProductRelatedComponents;
import com.setof.connectly.module.display.dto.component.QProductRelatedComponents;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.QComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.brand.QBrandComponentQueryDto;
import com.setof.connectly.module.display.dto.query.category.QCategoryComponentQueryDto;
import com.setof.connectly.module.display.dto.query.product.QProductComponentQueryDto;
import com.setof.connectly.module.display.dto.query.tab.QTabComponentQueryDto;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RelatedProductComponentRepositoryImpl implements RelatedProductComponentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ProductRelatedComponents fetchRelatedProductComponent(
            long contentId, List<Long> componentIds) {
        return queryFactory
                .from(component)
                .leftJoin(brandComponent)
                .on(brandComponent.component.id.eq(component.id))
                .on(brandComponent.deleteYn.eq(Yn.N))
                .leftJoin(brandComponentItem)
                .on(brandComponentItem.brandComponentId.eq(brandComponent.id))
                .on(brandComponentItem.deleteYn.eq(Yn.N))
                .leftJoin(brand)
                .on(brand.id.eq(brandComponentItem.brandId))
                .leftJoin(categoryComponent)
                .on(categoryComponent.component.id.eq(component.id))
                .on(categoryComponent.deleteYn.eq(Yn.N))
                .leftJoin(productComponent)
                .on(productComponent.component.id.eq(component.id))
                .on(productComponent.deleteYn.eq(Yn.N))
                .leftJoin(tabComponent)
                .on(tabComponent.component.id.eq(component.id))
                .on(tabComponent.deleteYn.eq(Yn.N))
                .leftJoin(tab)
                .on(tab.tabComponentId.eq(tabComponent.id))
                .on(tab.deleteYn.eq(Yn.N))
                .where(component.id.in(componentIds), component.deleteYn.eq(Yn.N))
                .distinct()
                .transform(
                        GroupBy.groupBy(component.contentId)
                                .as(
                                        new QProductRelatedComponents(
                                                component.contentId,
                                                GroupBy.set(
                                                        new QBrandComponentQueryDto(
                                                                        brandComponent.component.id,
                                                                        brandComponent.id,
                                                                        new QBrandDto(
                                                                                brandComponentItem
                                                                                        .brandId,
                                                                                brand.brandName),
                                                                        brandComponentItem
                                                                                .categoryId,
                                                                        component
                                                                                .componentDetails
                                                                                .orderType)
                                                                .skipNulls()),
                                                GroupBy.set(
                                                        new QCategoryComponentQueryDto(
                                                                        categoryComponent
                                                                                .component
                                                                                .id,
                                                                        categoryComponent.id,
                                                                        categoryComponent
                                                                                .categoryId,
                                                                        component
                                                                                .componentDetails
                                                                                .orderType)
                                                                .skipNulls()),
                                                GroupBy.set(
                                                        new QProductComponentQueryDto(
                                                                        productComponent
                                                                                .component
                                                                                .id,
                                                                        productComponent.id,
                                                                        component
                                                                                .componentDetails
                                                                                .orderType)
                                                                .skipNulls()),
                                                GroupBy.set(
                                                        new QTabComponentQueryDto(
                                                                        tabComponent.component.id,
                                                                        tabComponent.id,
                                                                        tab.id,
                                                                        tab.tabName,
                                                                        tabComponent.stickyYn,
                                                                        tabComponent.tabMovingType,
                                                                        tab.displayOrder,
                                                                        component
                                                                                .componentDetails
                                                                                .orderType)
                                                                .skipNulls()))))
                .get(contentId);
    }

    @Override
    public List<ComponentItemQueryDto> fetchComponentItemQueries(List<Long> componentIds) {
        return queryFactory
                .from(component)
                .innerJoin(componentTarget)
                .on(componentTarget.componentId.eq(component.id))
                .on(componentTarget.deleteYn.eq(Yn.N))
                .innerJoin(componentItem)
                .on(componentItem.componentTarget.id.eq(componentTarget.id))
                .on(componentItem.deleteYn.eq(Yn.N))
                .innerJoin(productGroup)
                .on(productGroup.id.eq(componentItem.productGroupId))
                .on(productGroup.productGroupDetails.productStatus.soldOutYn.eq(Yn.N))
                .on(productGroup.productGroupDetails.productStatus.displayYn.eq(Yn.Y))
                .on(productGroup.deleteYn.eq(Yn.N))
                .innerJoin(productGroupImage)
                .on(productGroupImage.productGroup.id.eq(componentItem.productGroupId))
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
                .where(component.id.in(componentIds), component.deleteYn.eq(Yn.N))
                .orderBy(componentItem.displayOrder.asc())
                .transform(
                        GroupBy.groupBy(componentItem.id)
                                .list(
                                        new QComponentItemQueryDto(
                                                        component.id,
                                                        componentTarget.id,
                                                        componentItem.id,
                                                        componentItem.productGroupId,
                                                        productGroup.productGroupDetails.sellerId,
                                                        new QBrandDto(
                                                                brand.id,
                                                                brand.brandName.coalesce("")),
                                                        componentItem.productDisplayName.coalesce(
                                                                productGroup
                                                                        .productGroupDetails
                                                                        .productGroupName),
                                                        componentItem.productDisplayImage.coalesce(
                                                                productGroupImage
                                                                        .imageDetail
                                                                        .imageUrl),
                                                        productGroup.productGroupDetails.price,
                                                        componentItem.displayOrder,
                                                        component.componentDetails.componentType,
                                                        componentTarget.tabId,
                                                        componentTarget.sortType,
                                                        productGroup.insertDate,
                                                        productRatingStats.averageRating.coalesce(
                                                                0.0),
                                                        productRatingStats.reviewCount.coalesce(0L),
                                                        productScore.score.coalesce(0.0),
                                                        component.componentDetails.orderType)
                                                .skipNulls()));
    }

    @Override
    public List<ComponentItemQueryDto> fetchComponentItemQueries(
            List<Long> componentIds, ComponentFilter filter) {
        return queryFactory
                .from(component)
                .innerJoin(componentTarget)
                .on(componentTarget.componentId.eq(component.id))
                .on(componentTarget.deleteYn.eq(Yn.N))
                .innerJoin(componentItem)
                .on(componentItem.componentTarget.id.eq(componentTarget.id))
                .on(componentItem.deleteYn.eq(Yn.N))
                .innerJoin(productGroup)
                .on(productGroup.id.eq(componentItem.productGroupId))
                .on(productGroup.productGroupDetails.productStatus.soldOutYn.eq(Yn.N))
                .on(productGroup.productGroupDetails.productStatus.displayYn.eq(Yn.Y))
                .on(productGroup.deleteYn.eq(Yn.N))
                .innerJoin(productGroupImage)
                .on(productGroupImage.productGroup.id.eq(componentItem.productGroupId))
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
                        component.id.in(componentIds),
                        component.deleteYn.eq(Yn.N),
                        categoryIdIn(filter),
                        brandIdEq(filter.getBrandId()),
                        brandIdIn(filter))
                .orderBy(componentItem.displayOrder.asc())
                .transform(
                        GroupBy.groupBy(componentItem.id)
                                .list(
                                        new QComponentItemQueryDto(
                                                        component.id,
                                                        componentTarget.id,
                                                        componentItem.id,
                                                        componentItem.productGroupId,
                                                        productGroup.productGroupDetails.sellerId,
                                                        new QBrandDto(
                                                                brand.id,
                                                                brand.brandName.coalesce("")),
                                                        componentItem.productDisplayName.coalesce(
                                                                productGroup
                                                                        .productGroupDetails
                                                                        .productGroupName),
                                                        componentItem.productDisplayImage.coalesce(
                                                                productGroupImage
                                                                        .imageDetail
                                                                        .imageUrl),
                                                        productGroup.productGroupDetails.price,
                                                        componentItem.displayOrder,
                                                        component.componentDetails.componentType,
                                                        componentTarget.tabId,
                                                        componentTarget.sortType,
                                                        productGroup.insertDate,
                                                        productRatingStats.averageRating.coalesce(
                                                                0.0),
                                                        productRatingStats.reviewCount.coalesce(0L),
                                                        productScore.score.coalesce(0.0),
                                                        component.componentDetails.orderType)
                                                .skipNulls()));
    }

    protected BooleanExpression brandIdEq(Long brandId) {
        if (brandId != null) return productGroup.productGroupDetails.brandId.eq(brandId);
        return null;
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

    protected BooleanExpression categoryIdIn(ComponentFilter filter) {
        List<Long> categoryIds = filter.getCategoryIds();
        if (categoryIds.isEmpty()) return null;
        return productGroup.productGroupDetails.categoryId.in(filter.getCategoryIds());
    }
}
