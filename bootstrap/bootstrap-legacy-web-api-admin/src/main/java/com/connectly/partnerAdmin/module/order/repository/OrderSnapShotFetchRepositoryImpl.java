package com.connectly.partnerAdmin.module.order.repository;


import com.connectly.partnerAdmin.module.brand.core.QBaseBrandContext;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.order.dto.*;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.product.dto.option.QOptionDto;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.connectly.partnerAdmin.module.brand.entity.QBrand.brand;
import static com.connectly.partnerAdmin.module.order.entity.QOrder.order;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.QOrderSnapShotProductDelivery.orderSnapShotProductDelivery;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.image.QOrderSnapShotProductGroupImage.orderSnapShotProductGroupImage;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.QOrderSnapShotMileage.orderSnapShotMileage;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.QOrderSnapShotMileageDetail.orderSnapShotMileageDetail;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.option.QOrderSnapShotOptionDetail.orderSnapShotOptionDetail;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.option.QOrderSnapShotOptionGroup.orderSnapShotOptionGroup;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.option.QOrderSnapShotProductOption.orderSnapShotProductOption;
import static com.connectly.partnerAdmin.module.product.entity.delivery.QProductDelivery.productDelivery;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupDetailDescription.productGroupDetailDescription;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.connectly.partnerAdmin.module.product.entity.notice.QProductNotice.productNotice;
import static com.connectly.partnerAdmin.module.product.entity.option.QOptionDetail.optionDetail;
import static com.connectly.partnerAdmin.module.product.entity.option.QOptionGroup.optionGroup;
import static com.connectly.partnerAdmin.module.product.entity.option.QProductOption.productOption;
import static com.connectly.partnerAdmin.module.product.entity.stock.QProduct.product;
import static com.connectly.partnerAdmin.module.product.entity.stock.QProductStock.productStock;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;

@Repository
@RequiredArgsConstructor
public class OrderSnapShotFetchRepositoryImpl implements OrderSnapShotFetchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderProduct> fetchOrderProductOption(Long paymentId, List<Long> orderIds) {
        return queryFactory.from(order)
                        .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                        .innerJoin(order.orderSnapShotProductDelivery, orderSnapShotProductDelivery)
                        .innerJoin(order.orderSnapShotProductGroupImages, orderSnapShotProductGroupImage)
                            .on(orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                        .innerJoin(brand)
                            .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                        .innerJoin(seller)
                            .on(order.sellerId.eq(seller.id))
                        .leftJoin(order.orderSnapShotProductOptions, orderSnapShotProductOption)
                        .leftJoin(order.orderSnapShotOptionGroups, orderSnapShotOptionGroup)
                            .on(orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId.eq(orderSnapShotProductOption.snapShotProductOption.optionGroupId))
                        .leftJoin(order.orderSnapShotOptionDetails, orderSnapShotOptionDetail)
                            .on(orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId.eq(orderSnapShotProductOption.snapShotProductOption.optionDetailId))
                        .leftJoin(order.orderSnapShotMileage, orderSnapShotMileage)
                        .leftJoin(orderSnapShotMileage.mileageDetails, orderSnapShotMileageDetail)
                        .where(paymentIdEq(paymentId), orderIdIn(orderIds), exclusiveOrderStatus())
                        .groupBy(
                                order.id,
                                orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                                orderSnapShotProductGroup.snapShotProductGroup.optionType,
                                orderSnapShotProductGroup.snapShotProductGroup.managementType,
                                orderSnapShotProductGroup.snapShotProductGroup.price,
                                orderSnapShotProductGroup.snapShotProductGroup.productStatus,
                                orderSnapShotProductGroup.snapShotProductGroup.clothesDetailInfo,
                                orderSnapShotProductGroup.snapShotProductGroup.sellerId,
                                orderSnapShotProductGroup.snapShotProductGroup.categoryId,
                                orderSnapShotProductGroup.snapShotProductGroup.brandId,
                                brand.id,
                                brand.brandName,
                                orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                                orderSnapShotProductOption.snapShotProductOption.productId,
                                seller.sellerName,
                                orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.imageUrl,
                                orderSnapShotProductDelivery.snapShotProductDelivery.deliveryNotice.deliveryArea,
                                order.quantity,
                                order.orderStatus,
                                orderSnapShotProductGroup.snapShotProductGroup.price.regularPrice,
                                order.orderAmount,
                                orderSnapShotMileageDetail.usedAmount,
                                orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId,
                                orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId,
                                orderSnapShotOptionGroup.snapShotOptionGroup.optionName,
                                orderSnapShotOptionDetail.snapShotOptionDetail.optionValue
                        )
                        .distinct()
                        .transform(
                                GroupBy.groupBy(order.id).list(
                                        new QOrderProduct(
                                                order.id,
                                                new QProductGroupSnapShotDetails(
                                                        orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                                                        orderSnapShotProductGroup.snapShotProductGroup.optionType,
                                                        orderSnapShotProductGroup.snapShotProductGroup.managementType,
                                                        orderSnapShotProductGroup.snapShotProductGroup.price,
                                                        orderSnapShotProductGroup.snapShotProductGroup.productStatus,
                                                        orderSnapShotProductGroup.snapShotProductGroup.clothesDetailInfo,
                                                        orderSnapShotProductGroup.snapShotProductGroup.sellerId,
                                                        orderSnapShotProductGroup.snapShotProductGroup.categoryId,
                                                        orderSnapShotProductGroup.snapShotProductGroup.brandId
                                                ),
                                                new QBaseBrandContext(
                                                        brand.id,
                                                        brand.brandName
                                                ),
                                                orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                                                orderSnapShotProductOption.snapShotProductOption.productId,
                                                seller.sellerName,
                                                orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.imageUrl,
                                                orderSnapShotProductDelivery.snapShotProductDelivery.deliveryNotice.deliveryArea,
                                                order.quantity,
                                                order.orderStatus,
                                                orderSnapShotProductGroup.snapShotProductGroup.price.regularPrice,
                                                order.orderAmount,
                                                orderSnapShotMileageDetail.usedAmount.coalesce(BigDecimal.ZERO),
                                                GroupBy.set(
                                                        new QOptionDto(
                                                                orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId,
                                                                orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId,
                                                                orderSnapShotOptionGroup.snapShotOptionGroup.optionName,
                                                                orderSnapShotOptionDetail.snapShotOptionDetail.optionValue
                                                        )
                                                )
                                        )
                                )
                        );
    }


    @Override
    public List<OrderSnapShotProductGroupQueryDto> fetchOrderSnapShotProductGroupQueryDto(List<Long> productIds) {
        return queryFactory
                .from(productGroup)
                .join(productGroup.products, product)
                .join(productGroup.productDelivery, productDelivery)
                .join(productGroup.productNotice, productNotice)
                .join(productGroup.detailDescription, productGroupDetailDescription)
                .join(productGroup.images, productGroupImage)
                    .on(productGroupImage.deleteYn.eq(Yn.N))
                .where(
                        productIdIn(productIds)
                )
                .distinct()
                .transform(
                        GroupBy.groupBy(productGroup.id).list(
                                new QOrderSnapShotProductGroupQueryDto(
                                        productGroup,
                                        product,
                                        productDelivery,
                                        productNotice,
                                        productGroupDetailDescription,
                                        GroupBy.set(
                                                productGroupImage
                                        )
                                )
                        )
                );
    }

    @Override
    public List<OrderSnapShotProductQueryDto> fetchOrderSnapShotProductQueryDto(List<Long> productIds) {
        return queryFactory
                .from(product)
                .join(product.productStock, productStock)
                .leftJoin(product.productOptions, productOption)
                .leftJoin(productOption.optionGroup, optionGroup)
                .leftJoin(productOption.optionDetail, optionDetail)
                .where(
                        productIdIn(productIds)
                )
                .distinct()
                .transform(
                        GroupBy.groupBy(product.id).list(
                                new QOrderSnapShotProductQueryDto(
                                        product,
                                        productStock,
                                        GroupBy.set(
                                                productOption
                                        ),
                                        GroupBy.set(
                                                optionGroup
                                        ),
                                        GroupBy.set(
                                                optionDetail
                                        )
                                )
                        )
                );
    }



    private BooleanExpression productIdIn(List<Long> productIds){
        if(!productIds.isEmpty()) return product.id.in(productIds);
        return null;
    }

    private BooleanExpression orderIdIn(List<Long> orderIds) {
        if(!orderIds.isEmpty()) return order.id.in(orderIds);
        return null;
    }

    private BooleanExpression paymentIdEq(Long paymentId){
        if(paymentId != null) return order.payment.id.eq(paymentId);
        else return null;
    }

    private BooleanExpression exclusiveOrderStatus(){
        return order.orderStatus.notIn(OrderStatus.exclusiveOrderStatus());
    }

}
