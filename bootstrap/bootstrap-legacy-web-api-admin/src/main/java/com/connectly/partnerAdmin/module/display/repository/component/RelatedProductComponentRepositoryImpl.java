package com.connectly.partnerAdmin.module.display.repository.component;


import com.connectly.partnerAdmin.module.brand.core.QBaseBrandContext;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.ProductRelatedComponents;
import com.connectly.partnerAdmin.module.display.dto.component.QProductRelatedComponents;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.QComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.brand.QBrandComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.category.QCategoryComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.product.QProductComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.tab.QTabComponentQueryDto;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.connectly.partnerAdmin.module.brand.entity.QBrand.brand;
import static com.connectly.partnerAdmin.module.display.entity.component.QComponent.component;
import static com.connectly.partnerAdmin.module.display.entity.component.QComponentItem.componentItem;
import static com.connectly.partnerAdmin.module.display.entity.component.QComponentTarget.componentTarget;
import static com.connectly.partnerAdmin.module.display.entity.component.item.QBrandComponentItem.brandComponentItem;
import static com.connectly.partnerAdmin.module.display.entity.component.item.QTab.tab;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.product.QBrandComponent.brandComponent;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.product.QCategoryComponent.categoryComponent;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.product.QProductComponent.productComponent;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.product.QTabComponent.tabComponent;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupImage.productGroupImage;


@Repository
@RequiredArgsConstructor
public class RelatedProductComponentRepositoryImpl implements RelatedProductComponentRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public ProductRelatedComponents fetchRelatedProductComponent(long contentId, List<Long> componentIds){
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

                .where(componentIdIn(componentIds), deleteYn())
                .distinct()
                .transform(
                        GroupBy.groupBy(component.contentId).as(
                            new QProductRelatedComponents(
                                    component.contentId,
                                    GroupBy.set(
                                            new QBrandComponentQueryDto(
                                                    brandComponent.component.id,
                                                    brandComponent.id,
                                                    brandComponentItem.id,
                                                    new QBaseBrandContext(
                                                            brand.id,
                                                            brand.brandName
                                                            ),
                                                    brandComponentItem.categoryId,
                                                    component.componentDetails.orderType
                                            ).skipNulls()
                                    ),
                                    GroupBy.set(
                                            new QCategoryComponentQueryDto(
                                                    categoryComponent.component.id,
                                                    categoryComponent.id,
                                                    categoryComponent.categoryId,
                                                    component.componentDetails.orderType
                                            ).skipNulls()
                                    ),
                                    GroupBy.set(
                                            new QProductComponentQueryDto(
                                                    productComponent.component.id,
                                                    productComponent.id,
                                                    component.componentDetails.orderType
                                            ).skipNulls()
                                    ),
                                    GroupBy.set(
                                            new QTabComponentQueryDto(
                                                    tabComponent.component.id,
                                                    tabComponent.id,
                                                    tab.id,
                                                    tab.tabName,
                                                    tabComponent.stickyYn,
                                                    tabComponent.tabMovingType,
                                                    tab.displayOrder,
                                                    component.componentDetails.orderType
                                            ).skipNulls()
                                    )
                            )
                        )
                ).get(contentId);
    }


    @Override
    public List<ComponentItemQueryDto> fetchComponentItemQueries(List<Long> componentIds){
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
                    .on(productGroup.deleteYn.eq(Yn.N))
                .innerJoin(productGroupImage)
                    .on(productGroupImage.productGroup.id.eq(componentItem.productGroupId))
                    .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                    .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(brand)
                    .on(brand.id.eq(productGroup.productGroupDetails.brandId))

                .where(componentIdIn(componentIds), deleteYn())
                .transform(
                        GroupBy.groupBy(componentItem.id).list(
                                new QComponentItemQueryDto(
                                                component.id,
                                                componentTarget.id,
                                                componentItem.id,
                                                componentItem.productGroupId,
                                                new QBaseBrandContext(
                                                        brand.id,
                                                        brand.brandName.coalesce("")
                                                ),
                                                componentItem.productDisplayName.coalesce(productGroup.productGroupDetails.productGroupName),
                                                componentItem.productDisplayImage.coalesce(productGroupImage.imageDetail.imageUrl),
                                                productGroup.productGroupDetails.price,
                                                componentItem.displayOrder,
                                                component.componentDetails.componentType,
                                                componentTarget.tabId,
                                                componentTarget.sortType,
                                                component.componentDetails.orderType
                                ).skipNulls()
                        )
                );
    }


    private BooleanExpression componentIdIn(List<Long> componentIds){
        return component.id.in(componentIds);
    }

    private BooleanExpression deleteYn(){
        return component.deleteYn.eq(Yn.N);
    }


}
