package com.connectly.partnerAdmin.module.display.repository.component;


import com.connectly.partnerAdmin.module.brand.core.QBaseBrandContext;
import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.QDisplayProductGroupThumbnail;
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
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupImage.productGroupImage;


@Repository
@RequiredArgsConstructor
public class ComponentFetchRepositoryImpl implements ComponentFetchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<DisplayProductGroupThumbnail> getComponentRegisteredProducts(long componentId) {
        return queryFactory.from(component)
                .innerJoin(componentTarget).on(componentTarget.componentId.eq(component.id))
                .innerJoin(componentItem).on(componentItem.componentTarget.id.eq(componentTarget.id))
                .innerJoin(productGroup).on(componentItem.productGroupId.eq(productGroup.id))
                .innerJoin(productGroupImage)
                    .on(productGroupImage.productGroup.id.eq(productGroup.id))
                    .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                .innerJoin(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .orderBy(componentItem.displayOrder.asc())
                .where(componentIdEq(componentId))
                .transform(
                        GroupBy.groupBy(component.id).list(
                                new QDisplayProductGroupThumbnail(
                                        componentItem.productGroupId,
                                        productGroup.productGroupDetails.sellerId,
                                        componentItem.productDisplayName.coalesce(productGroup.productGroupDetails.productGroupName),
                                        new QBaseBrandContext(
                                                brand.id,
                                                brand.brandName
                                        ),
                                        componentItem.productDisplayImage.coalesce(productGroupImage.imageDetail.imageUrl),
                                        productGroup.productGroupDetails.price
                                )
                        )
                );
    }


    private BooleanExpression componentIdEq(long componentId) {
        return component.id.eq(componentId);
    }
}
