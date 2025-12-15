package com.setof.connectly.module.order.repository;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.event.entity.QEventProduct.eventProduct;
import static com.setof.connectly.module.order.entity.order.QOrder.order;
import static com.setof.connectly.module.order.entity.order.QOrderHistory.orderHistory;
import static com.setof.connectly.module.order.entity.snapshot.delivery.QOrderSnapShotProductDelivery.orderSnapShotProductDelivery;
import static com.setof.connectly.module.order.entity.snapshot.group.QOrderSnapShotProduct.orderSnapShotProduct;
import static com.setof.connectly.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.setof.connectly.module.order.entity.snapshot.image.QOrderSnapShotProductGroupImage.orderSnapShotProductGroupImage;
import static com.setof.connectly.module.order.entity.snapshot.mileage.QOrderSnapShotMileage.orderSnapShotMileage;
import static com.setof.connectly.module.order.entity.snapshot.mileage.QOrderSnapShotMileageDetail.orderSnapShotMileageDetail;
import static com.setof.connectly.module.order.entity.snapshot.option.QOrderSnapShotOptionDetail.orderSnapShotOptionDetail;
import static com.setof.connectly.module.order.entity.snapshot.option.QOrderSnapShotOptionGroup.orderSnapShotOptionGroup;
import static com.setof.connectly.module.order.entity.snapshot.option.QOrderSnapShotProductOption.orderSnapShotProductOption;
import static com.setof.connectly.module.payment.entity.QPayment.payment;
import static com.setof.connectly.module.payment.entity.QPaymentBill.paymentBill;
import static com.setof.connectly.module.payment.entity.QPaymentMethod.paymentMethod;
import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotMileage.paymentSnapShotMileage;
import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotShippingAddress.paymentSnapShotShippingAddress;
import static com.setof.connectly.module.seller.entity.QSeller.seller;
import static com.setof.connectly.module.seller.entity.QSellerBusinessInfo.sellerBusinessInfo;
import static com.setof.connectly.module.seller.entity.QSellerShippingInfo.sellerShippingInfo;
import static com.setof.connectly.module.shipment.entity.QShipment.shipment;
import static com.setof.connectly.module.user.entity.QUserGrade.userGrade;
import static com.setof.connectly.module.user.entity.QUsers.users;

import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.event.enums.EventProductType;
import com.setof.connectly.module.mileage.dto.query.PendingMileageQueryDto;
import com.setof.connectly.module.mileage.dto.query.QPendingMileageQueryDto;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.notification.dto.order.QProductOrderSheet;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.dto.OrderRejectReason;
import com.setof.connectly.module.order.dto.QOrderProductDto;
import com.setof.connectly.module.order.dto.QOrderRejectReason;
import com.setof.connectly.module.order.dto.fetch.*;
import com.setof.connectly.module.order.dto.filter.OrderFilter;
import com.setof.connectly.module.order.dto.query.QOrderAmountDto;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.payment.dto.payment.QPaymentDetail;
import com.setof.connectly.module.payment.dto.receiver.QReceiverInfo;
import com.setof.connectly.module.payment.dto.refund.QRefundNotice;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.dto.option.QOptionDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import com.setof.connectly.module.review.dto.QReviewOrderProductDto;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import com.setof.connectly.module.shipment.dto.QPaymentShipmentInfo;
import com.setof.connectly.module.shipment.enums.ShipmentCompanyCode;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderFindRepositoryImpl implements OrderFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> fetchOrderEntity(long orderId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(order).where(orderIdEq(orderId)).fetchFirst());
    }

    @Override
    public List<Order> fetchOrderEntities(long paymentId) {
        return queryFactory.selectFrom(order).where(paymentIdEq(paymentId)).fetch();
    }

    @Override
    public Map<OrderStatus, Long> countOrdersByStatusInMyPage(
            long userId, List<OrderStatus> orderStatuses) {
        List<Tuple> results =
                queryFactory
                        .select(order.orderStatus, order.count())
                        .from(order)
                        .where(
                                userIdEq(userId),
                                orderStatusIn(orderStatuses),
                                defaultBetweenOrder())
                        .groupBy(order.orderStatus)
                        .fetch();

        Map<OrderStatus, Long> orderStatusCounts = new HashMap<>();
        for (Tuple result : results) {
            orderStatusCounts.put(result.get(order.orderStatus), result.get(order.count()));
        }

        return orderStatusCounts;
    }

    private List<Long> fetchOrderIds(long userId, OrderFilter filter, Pageable pageable) {
        return queryFactory
                .select(order.id)
                .from(order)
                .where(
                        orderIdLt(filter.getLastDomainId()),
                        betweenTime(filter),
                        orderStatusIn(filter.getOrderStatusList()),
                        userIdEq(userId))
                .orderBy(order.id.desc())
                .limit(pageable.getPageSize() + 1)
                .distinct()
                .fetch();
    }

    @Override
    public List<OrderResponse> fetchOrders(long userId, OrderFilter filterDto, Pageable pageable) {
        List<Long> orderIds = fetchOrderIds(userId, filterDto, pageable);

        return queryFactory
                .from(order)
                .innerJoin(payment)
                .on(payment.id.eq(order.paymentId))
                .innerJoin(paymentBill)
                .on(payment.id.eq(paymentBill.paymentId))
                .innerJoin(paymentMethod)
                .on(paymentMethod.id.eq(paymentBill.paymentMethodId))
                .innerJoin(shipment)
                .on(shipment.orderId.eq(order.id))
                .innerJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(order.id))
                .innerJoin(orderSnapShotProductDelivery)
                .on(orderSnapShotProductDelivery.orderId.eq(order.id))
                .innerJoin(orderSnapShotProduct)
                .on(orderSnapShotProduct.orderId.eq(order.id))
                .innerJoin(brand)
                .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(seller)
                .on(seller.id.eq(order.sellerId))
                .innerJoin(sellerShippingInfo)
                .on(sellerShippingInfo.id.eq(seller.id))
                .innerJoin(users)
                .on(users.id.eq(order.userId))
                .innerJoin(paymentSnapShotShippingAddress)
                .on(paymentSnapShotShippingAddress.paymentId.eq(order.paymentId))
                .leftJoin(paymentSnapShotMileage)
                .on(paymentSnapShotMileage.paymentId.eq(payment.id))
                .innerJoin(orderSnapShotProductGroupImage)
                .on(orderSnapShotProductGroupImage.orderId.eq(order.id))
                .on(
                        orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail
                                .productGroupImageType.eq(ProductGroupImageType.MAIN))
                .leftJoin(orderSnapShotProductOption)
                .on(orderSnapShotProductOption.orderId.eq(order.id))
                .leftJoin(orderSnapShotOptionGroup)
                .on(orderSnapShotOptionGroup.orderId.eq(order.id))
                .on(
                        orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionGroupId))
                .leftJoin(orderSnapShotOptionDetail)
                .on(orderSnapShotOptionDetail.orderId.eq(order.id))
                .on(
                        orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionDetailId))
                .where(orderIdIn(orderIds))
                .orderBy(order.id.desc())
                .transform(
                        GroupBy.groupBy(order.id)
                                .list(
                                        new QOrderResponse(
                                                new QPaymentDetail(
                                                        payment.id,
                                                        paymentBill.paymentAgencyId.coalesce(""),
                                                        payment.paymentDetails.paymentStatus,
                                                        paymentMethod.paymentMethodEnum,
                                                        payment.paymentDetails.paymentDate,
                                                        payment.paymentDetails.canceledDate,
                                                        payment.paymentDetails.userId,
                                                        payment.paymentDetails.siteName,
                                                        paymentBill.paymentAmount,
                                                        paymentSnapShotMileage.usedMileageAmount
                                                                .coalesce(0.0),
                                                        paymentBill.cardName.coalesce(""),
                                                        paymentBill.cardNumber.coalesce(""),
                                                        GroupBy.set(order.id)),
                                                new QOrderProductDto(
                                                        order.paymentId,
                                                        seller.id,
                                                        order.id,
                                                        new QBrandDto(brand.id, brand.brandName),
                                                        orderSnapShotProductGroup
                                                                .snapShotProductGroup
                                                                .productGroupId,
                                                        orderSnapShotProductGroup
                                                                .snapShotProductGroup
                                                                .productGroupName,
                                                        orderSnapShotProduct
                                                                .snapShotProduct
                                                                .productId,
                                                        seller.sellerName,
                                                        orderSnapShotProductGroupImage
                                                                .snapShotProductGroupImage
                                                                .imageDetail
                                                                .imageUrl,
                                                        order.quantity,
                                                        order.orderStatus,
                                                        orderSnapShotProductGroup
                                                                .snapShotProductGroup
                                                                .price
                                                                .regularPrice,
                                                        orderSnapShotProductGroup
                                                                .snapShotProductGroup
                                                                .price
                                                                .salePrice,
                                                        orderSnapShotProductGroup
                                                                .snapShotProductGroup
                                                                .price
                                                                .directDiscountPrice,
                                                        order.orderAmount,
                                                        GroupBy.set(
                                                                new QOptionDto(
                                                                        orderSnapShotOptionGroup
                                                                                .snapShotOptionGroup
                                                                                .optionGroupId,
                                                                        orderSnapShotOptionDetail
                                                                                .snapShotOptionDetail
                                                                                .optionDetailId,
                                                                        orderSnapShotOptionGroup
                                                                                .snapShotOptionGroup
                                                                                .optionName,
                                                                        orderSnapShotOptionDetail
                                                                                .snapShotOptionDetail
                                                                                .optionValue)),
                                                        new QRefundNotice(
                                                                orderSnapShotProductDelivery
                                                                        .snapShotProductDelivery
                                                                        .refundNotice
                                                                        .returnMethodDomestic,
                                                                orderSnapShotProductDelivery
                                                                        .snapShotProductDelivery
                                                                        .refundNotice
                                                                        .returnCourierDomestic,
                                                                orderSnapShotProductDelivery
                                                                        .snapShotProductDelivery
                                                                        .refundNotice
                                                                        .returnChargeDomestic,
                                                                sellerShippingInfo
                                                                        .returnAddressLine1
                                                                        .concat(" ")
                                                                        .concat(
                                                                                sellerShippingInfo
                                                                                        .returnAddressLine2)),
                                                        new QPaymentShipmentInfo(
                                                                shipment.orderId,
                                                                shipment.deliveryStatus,
                                                                shipment.companyCode.coalesce(
                                                                        ShipmentCompanyCode
                                                                                .REFER_DETAIL),
                                                                shipment.invoiceNo,
                                                                shipment.insertDate),
                                                        order.reviewYn),
                                                paymentBill.buyerInfo,
                                                new QReceiverInfo(
                                                        paymentSnapShotShippingAddress
                                                                .shippingDetails
                                                                .receiverName,
                                                        users.phoneNumber,
                                                        paymentSnapShotShippingAddress
                                                                .shippingDetails
                                                                .addressLine1,
                                                        paymentSnapShotShippingAddress
                                                                .shippingDetails
                                                                .addressLine2,
                                                        paymentSnapShotShippingAddress
                                                                .shippingDetails
                                                                .zipCode,
                                                        paymentSnapShotShippingAddress
                                                                .shippingDetails
                                                                .country,
                                                        paymentSnapShotShippingAddress
                                                                .shippingDetails
                                                                .deliveryRequest,
                                                        paymentSnapShotShippingAddress
                                                                .shippingDetails
                                                                .phoneNumber))));
    }

    @Override
    public List<OrderProductDto> fetchOrderProducts(
            List<Long> orderIds, List<OrderStatus> orderStatuses) {
        return queryFactory
                .from(order)
                .innerJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(order.id))
                .innerJoin(orderSnapShotProductDelivery)
                .on(orderSnapShotProductDelivery.orderId.eq(order.id))
                .innerJoin(orderSnapShotProduct)
                .on(orderSnapShotProduct.orderId.eq(order.id))
                .innerJoin(brand)
                .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(seller)
                .on(seller.id.eq(order.sellerId))
                .innerJoin(users)
                .on(users.id.eq(order.userId))
                .innerJoin(sellerShippingInfo)
                .on(sellerShippingInfo.id.eq(seller.id))
                .innerJoin(orderSnapShotProductGroupImage)
                .on(orderSnapShotProductGroupImage.orderId.eq(order.id))
                .on(
                        orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail
                                .productGroupImageType.eq(ProductGroupImageType.MAIN))
                .innerJoin(shipment)
                .on(shipment.orderId.eq(order.id))
                .leftJoin(orderSnapShotProductOption)
                .on(orderSnapShotProductOption.orderId.eq(order.id))
                .leftJoin(orderSnapShotOptionGroup)
                .on(orderSnapShotOptionGroup.orderId.eq(order.id))
                .on(
                        orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionGroupId))
                .leftJoin(orderSnapShotOptionDetail)
                .on(orderSnapShotOptionDetail.orderId.eq(order.id))
                .on(
                        orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionDetailId))
                .where(orderIdIn(orderIds), orderStatusIn(orderStatuses))
                .orderBy(order.id.desc())
                .groupBy(
                        order.paymentId,
                        seller.id,
                        order.id,
                        brand.id,
                        brand.brandName,
                        orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                        orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                        orderSnapShotProductGroup.snapShotProductGroup.price.regularPrice,
                        orderSnapShotProductGroup.snapShotProductGroup.price.salePrice,
                        orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice,
                        orderSnapShotProduct.snapShotProduct.productId,
                        seller.sellerName,
                        orderSnapShotProductGroupImage
                                .snapShotProductGroupImage
                                .imageDetail
                                .imageUrl,
                        order.quantity,
                        order.orderStatus,
                        order.orderAmount,
                        orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId,
                        orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId,
                        orderSnapShotOptionGroup.snapShotOptionGroup.optionName,
                        orderSnapShotOptionDetail.snapShotOptionDetail.optionValue,
                        orderSnapShotProductDelivery
                                .snapShotProductDelivery
                                .refundNotice
                                .returnMethodDomestic,
                        orderSnapShotProductDelivery
                                .snapShotProductDelivery
                                .refundNotice
                                .returnCourierDomestic,
                        orderSnapShotProductDelivery
                                .snapShotProductDelivery
                                .refundNotice
                                .returnChargeDomestic,
                        sellerShippingInfo.returnAddressLine1,
                        shipment.orderId,
                        shipment.deliveryStatus,
                        shipment.companyCode.coalesce(ShipmentCompanyCode.REFER_DETAIL),
                        shipment.invoiceNo,
                        shipment.insertDate)
                .transform(
                        GroupBy.groupBy(order.id)
                                .list(
                                        new QOrderProductDto(
                                                order.paymentId,
                                                seller.id,
                                                order.id,
                                                new QBrandDto(brand.id, brand.brandName),
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupId,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupName,
                                                orderSnapShotProduct.snapShotProduct.productId,
                                                seller.sellerName,
                                                orderSnapShotProductGroupImage
                                                        .snapShotProductGroupImage
                                                        .imageDetail
                                                        .imageUrl,
                                                order.quantity,
                                                order.orderStatus,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .price
                                                        .regularPrice,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .price
                                                        .salePrice,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .price
                                                        .directDiscountPrice,
                                                order.orderAmount,
                                                GroupBy.set(
                                                        new QOptionDto(
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionGroupId,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionDetailId,
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionName,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionValue)),
                                                new QRefundNotice(
                                                        orderSnapShotProductDelivery
                                                                .snapShotProductDelivery
                                                                .refundNotice
                                                                .returnMethodDomestic,
                                                        orderSnapShotProductDelivery
                                                                .snapShotProductDelivery
                                                                .refundNotice
                                                                .returnCourierDomestic,
                                                        orderSnapShotProductDelivery
                                                                .snapShotProductDelivery
                                                                .refundNotice
                                                                .returnChargeDomestic,
                                                        sellerShippingInfo.returnAddressLine1),
                                                new QPaymentShipmentInfo(
                                                        shipment.orderId,
                                                        shipment.deliveryStatus,
                                                        shipment.companyCode.coalesce(
                                                                ShipmentCompanyCode.REFER_DETAIL),
                                                        shipment.invoiceNo,
                                                        shipment.insertDate),
                                                order.reviewYn)));
    }

    @Override
    public List<ReviewOrderProductDto> fetchAvailableReviews(
            long userId, Long lastDomainId, Pageable pageable) {
        List<Long> orderIds = fetchAvailableReviewOrderIds(userId, lastDomainId, pageable);

        return queryFactory
                .from(order)
                .innerJoin(payment)
                .on(payment.id.eq(order.paymentId))
                .innerJoin(seller)
                .on(seller.id.eq(order.sellerId))
                .innerJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(order.id))
                .innerJoin(brand)
                .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(orderSnapShotProductGroupImage)
                .on(orderSnapShotProductGroupImage.orderId.eq(order.id))
                .on(
                        orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail
                                .productGroupImageType.eq(ProductGroupImageType.MAIN))
                .innerJoin(orderSnapShotProduct)
                .on(orderSnapShotProduct.orderId.eq(order.id))
                .leftJoin(orderSnapShotProductOption)
                .on(orderSnapShotProductOption.orderId.eq(order.id))
                .leftJoin(orderSnapShotOptionGroup)
                .on(orderSnapShotOptionGroup.orderId.eq(order.id))
                .on(
                        orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionGroupId))
                .leftJoin(orderSnapShotOptionDetail)
                .on(orderSnapShotOptionDetail.orderId.eq(order.id))
                .on(
                        orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionDetailId))
                .where(orderIdIn(orderIds))
                .transform(
                        GroupBy.groupBy(order.id)
                                .list(
                                        new QReviewOrderProductDto(
                                                payment.id,
                                                order.sellerId,
                                                order.id,
                                                new QBrandDto(brand.id, brand.brandName),
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupId,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupName,
                                                orderSnapShotProduct.snapShotProduct.productId,
                                                seller.sellerName,
                                                orderSnapShotProductGroupImage
                                                        .snapShotProductGroupImage
                                                        .imageDetail
                                                        .imageUrl,
                                                order.quantity,
                                                order.orderStatus,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .price
                                                        .regularPrice,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .price
                                                        .currentPrice,
                                                order.orderAmount,
                                                GroupBy.set(
                                                        new QOptionDto(
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionGroupId,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionDetailId,
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionName,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionValue)),
                                                payment.paymentDetails.paymentDate)));
    }

    @Override
    public JPAQuery<Long> fetchAvailableReviewsCountQuery(long userId) {
        return queryFactory
                .select(order.count())
                .from(order)
                .where(
                        userIdEq(userId),
                        reviewNotYet(),
                        orderStatusIn(OrderStatus.getAvailableReviewOrderStatus()))
                .distinct();
    }

    @Override
    public Optional<OrderHistoryResponse> fetchOrderHistory(long orderId, OrderStatus orderStatus) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QOrderHistoryResponse(
                                        orderHistory.orderId,
                                        orderHistory.orderStatus,
                                        orderHistory.updateDate))
                        .from(orderHistory)
                        .where(orderHistoryIdEq(orderId), orderHistoryOrderStatusEq(orderStatus))
                        .fetchOne());
    }

    @Override
    public List<OrderHistoryResponse> fetchOrderHistories(long orderId) {
        return queryFactory
                .from(orderHistory)
                .leftJoin(shipment)
                .on(shipment.orderId.eq(orderHistory.orderId))
                .where(orderHistoryIdEq(orderId))
                .transform(
                        GroupBy.groupBy(orderHistory.orderId)
                                .list(
                                        new QOrderHistoryResponse(
                                                orderHistory.orderId,
                                                orderHistory.changeReason.coalesce(""),
                                                orderHistory.changeDetailReason.coalesce(""),
                                                orderHistory.orderStatus,
                                                shipment.invoiceNo.coalesce(""),
                                                shipment.companyCode.coalesce(
                                                        ShipmentCompanyCode.REFER_DETAIL),
                                                orderHistory.updateDate)));
    }

    @Override
    public List<OrderRejectReason> fetchRejectedOrder(List<Long> orderIds) {
        return queryFactory
                .from(order)
                .innerJoin(orderHistory)
                .on(order.id.eq(orderHistory.orderId))
                .where(
                        orderIdIn(orderIds),
                        orderHistoryOrderStatusIn(OrderStatus.getRejectedOrderStatus()))
                .orderBy(orderHistory.id.desc())
                .transform(
                        GroupBy.groupBy(order.id)
                                .list(
                                        new QOrderRejectReason(
                                                order.id,
                                                orderHistory.orderStatus,
                                                orderHistory.changeReason,
                                                orderHistory.changeDetailReason,
                                                orderHistory.insertDate)));
    }

    private List<Long> fetchAvailableReviewOrderIds(
            long userId, Long lastDomainId, Pageable pageable) {
        return queryFactory
                .select(order.id)
                .from(order)
                .where(
                        userIdEq(userId),
                        reviewNotYet(),
                        orderIdLt(lastDomainId),
                        orderStatusIn(OrderStatus.getAvailableReviewOrderStatus()),
                        isLesserThan3Months())
                .orderBy(order.id.desc())
                .limit(pageable.getPageSize() + 1)
                .distinct()
                .fetch();
    }

    @Override
    public Optional<PendingMileageQueryDto> fetchPendingMileage(long paymentId) {
        return Optional.ofNullable(
                queryFactory
                        .from(order)
                        .innerJoin(payment)
                        .on(payment.id.eq(order.paymentId))
                        .innerJoin(users)
                        .on(users.id.eq(order.userId))
                        .innerJoin(userGrade)
                        .on(userGrade.id.eq(users.userGradeId))
                        .where(paymentIdEq(paymentId))
                        .distinct()
                        .transform(
                                GroupBy.groupBy(payment.id)
                                        .as(
                                                new QPendingMileageQueryDto(
                                                        userGrade.gradeName,
                                                        GroupBy.set(
                                                                new QOrderAmountDto(
                                                                        order.id,
                                                                        order.orderAmount)))))
                        .get(paymentId));
    }

    @Override
    public List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds) {

        return queryFactory
                .from(order)
                .innerJoin(payment)
                .on(payment.id.eq(order.paymentId))
                .innerJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(order.id))
                .innerJoin(users)
                .on(users.id.eq(order.userId))
                .innerJoin(sellerBusinessInfo)
                .on(sellerBusinessInfo.id.eq(order.sellerId))
                .leftJoin(orderSnapShotMileage)
                .on(orderSnapShotMileage.orderId.eq(order.id))
                .leftJoin(orderSnapShotMileageDetail)
                .on(orderSnapShotMileageDetail.orderSnapShotMileageId.eq(orderSnapShotMileage.id))
                .where(orderIdIn(orderIds))
                .transform(
                        GroupBy.groupBy(order.id)
                                .list(
                                        new QProductOrderSheet(
                                                payment.id,
                                                payment.paymentDetails.paymentAmount,
                                                order.id,
                                                order.orderAmount,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupId,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupName,
                                                sellerBusinessInfo.csPhoneNumber,
                                                users.phoneNumber.coalesce(""),
                                                GroupBy.list(orderSnapShotMileageDetail.usedAmount),
                                                order.orderStatus)));
    }

    @Override
    public List<OrderProductDto> fetchProductsOrderedWithinPeriod(
            long userId, Set<Long> productIds, DisplayPeriod displayPeriod) {
        return queryFactory
                .from(order)
                .where(
                        userIdEq(userId),
                        orderProductIdIn(productIds),
                        orderWithinPeriod(displayPeriod),
                        orderStatusIn(OrderStatus.getAvailableStatusBuyingEventProducts()))
                .orderBy(order.id.desc())
                .distinct()
                .transform(
                        GroupBy.groupBy(order.id)
                                .list(
                                        new QOrderProductDto(
                                                order.id,
                                                order.productId,
                                                order.quantity,
                                                order.orderStatus)));
    }

    @Override
    public boolean isRaffleOrderProduct(long orderId) {
        Long aLong =
                queryFactory
                        .select(orderSnapShotProductGroup.id)
                        .from(orderSnapShotProductGroup)
                        .innerJoin(eventProduct)
                        .on(
                                eventProduct.productGroupId.eq(
                                        orderSnapShotProductGroup
                                                .snapShotProductGroup
                                                .productGroupId))
                        .on(eventProduct.eventProductType.eq(EventProductType.RAFFLE))
                        .where(orderSnapShotProductGroup.orderId.eq(orderId))
                        .fetchOne();

        return aLong != null;
    }

    private BooleanExpression isLesserThan3Months() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return order.updateDate.after(threeMonthsAgo);
    }

    private BooleanExpression betweenTime(OrderFilter filter) {
        return order.insertDate.between(filter.getStartDate(), filter.getEndDate());
    }

    private BooleanExpression orderIdEq(long orderId) {
        return order.id.eq(orderId);
    }

    private BooleanExpression orderIdIn(List<Long> orderIds) {
        return order.id.in(orderIds);
    }

    private BooleanExpression paymentIdEq(long paymentId) {
        return order.paymentId.eq(paymentId);
    }

    private BooleanExpression userIdEq(long userId) {
        return order.userId.eq(userId);
    }

    private BooleanExpression reviewNotYet() {
        return order.reviewYn.eq(Yn.N);
    }

    private BooleanExpression orderStatusIn(List<OrderStatus> orderStatuses) {
        if (orderStatuses != null) {
            if (!orderStatuses.isEmpty()) return order.orderStatus.in(orderStatuses);
        }
        return null;
    }

    private BooleanExpression orderHistoryOrderStatusIn(List<OrderStatus> orderStatuses) {
        if (orderStatuses != null) {
            if (!orderStatuses.isEmpty()) return orderHistory.orderStatus.in(orderStatuses);
        }
        return null;
    }

    private BooleanExpression defaultBetweenOrder() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return order.updateDate.after(threeMonthsAgo);
    }

    private BooleanExpression orderIdLt(Long lastDomainId) {
        if (lastDomainId != null) return order.id.lt(lastDomainId);
        return null;
    }

    private BooleanExpression orderHistoryIdEq(long orderId) {
        return orderHistory.orderId.eq(orderId);
    }

    private BooleanExpression orderHistoryOrderStatusEq(OrderStatus orderStatus) {
        return orderHistory.orderStatus.eq(orderStatus);
    }

    private BooleanExpression orderProductIdIn(Set<Long> productIds) {
        return order.productId.in(productIds);
    }

    private BooleanExpression orderWithinPeriod(DisplayPeriod displayPeriod) {
        return order.insertDate.between(
                displayPeriod.getDisplayStartDate(), displayPeriod.getDisplayEndDate());
    }
}
