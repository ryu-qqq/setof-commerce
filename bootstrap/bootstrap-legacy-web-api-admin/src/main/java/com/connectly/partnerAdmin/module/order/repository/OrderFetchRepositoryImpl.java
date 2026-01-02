package com.connectly.partnerAdmin.module.order.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.common.filter.BaseDateTimeRangeFilter;
import com.connectly.partnerAdmin.module.common.filter.BaseRoleFilter;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.notification.mapper.order.ProductOrderSheet;
import com.connectly.partnerAdmin.module.notification.mapper.order.QProductOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.OrderDashboardRanking;
import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderStatistics;
import com.connectly.partnerAdmin.module.order.dto.OrderTodayCountResponse;
import com.connectly.partnerAdmin.module.order.dto.QOrderDashboardRanking;
import com.connectly.partnerAdmin.module.order.dto.QOrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.dto.QOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.QOrderStatistics;
import com.connectly.partnerAdmin.module.order.dto.QSettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.SettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.response.OrderListResponse;
import com.connectly.partnerAdmin.module.order.dto.response.QOrderListResponse;
import com.connectly.partnerAdmin.module.order.dto.settlement.QSettlementInfo;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.order.filter.OrderFilter;
import com.connectly.partnerAdmin.module.payment.dto.payment.QPaymentDetail;
import com.connectly.partnerAdmin.module.payment.dto.receiver.QReceiverInfo;
import com.connectly.partnerAdmin.module.payment.dto.shipment.QPaymentShipmentInfo;
import com.connectly.partnerAdmin.module.shipment.enums.DeliveryStatus;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.connectly.partnerAdmin.module.brand.entity.QBrand.brand;
import static com.connectly.partnerAdmin.module.order.entity.QOrder.order;
import static com.connectly.partnerAdmin.module.order.entity.QOrderHistory.orderHistory;
import static com.connectly.partnerAdmin.module.order.entity.QSettlement.settlement;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.QOrderSnapShotProductDelivery.orderSnapShotProductDelivery;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.image.QOrderSnapShotProductGroupDetailDescription.orderSnapShotProductGroupDetailDescription;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.image.QOrderSnapShotProductGroupImage.orderSnapShotProductGroupImage;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.QOrderSnapShotMileage.orderSnapShotMileage;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.QOrderSnapShotMileageDetail.orderSnapShotMileageDetail;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.notice.QOrderSnapShotProductNotice.orderSnapShotProductNotice;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.stock.QOrderSnapShotProduct.orderSnapShotProduct;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.stock.QOrderSnapShotProductStock.orderSnapShotProductStock;
import static com.connectly.partnerAdmin.module.payment.entity.QPayment.payment;
import static com.connectly.partnerAdmin.module.payment.entity.QPaymentBill.paymentBill;
import static com.connectly.partnerAdmin.module.payment.entity.QPaymentMethod.paymentMethod;
import static com.connectly.partnerAdmin.module.payment.entity.QVBankAccount.vBankAccount;
import static com.connectly.partnerAdmin.module.payment.entity.snapshot.QPaymentSnapShotMileage.paymentSnapShotMileage;
import static com.connectly.partnerAdmin.module.payment.entity.snapshot.QPaymentSnapShotShippingAddress.paymentSnapShotShippingAddress;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;
import static com.connectly.partnerAdmin.module.seller.entity.QSellerBusinessInfo.sellerBusinessInfo;
import static com.connectly.partnerAdmin.module.shipment.entity.QShipment.shipment;
import static com.connectly.partnerAdmin.module.user.entity.QUsers.users;

@Repository
public class OrderFetchRepositoryImpl extends AbstractCommonRepository implements OrderFetchRepository {

    public OrderFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public Optional<Order> fetchOrderEntity(long orderId, Optional<Long> sellerIdOpt) {
        return Optional.ofNullable(
                getQueryFactory()
                        .selectFrom(order)
                        .innerJoin(order.shipment, shipment).fetchJoin()
                        .innerJoin(order.payment, payment).fetchJoin()
                        .innerJoin(payment.paymentBill, paymentBill).fetchJoin()
                        .innerJoin(paymentBill.paymentMethod, paymentMethod).fetchJoin()
                        .innerJoin(payment.paymentSnapShotShippingAddress, paymentSnapShotShippingAddress).fetchJoin()
                        .innerJoin(order.orderSnapShotProduct, orderSnapShotProduct).fetchJoin()
                        .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup).fetchJoin()
                        .innerJoin(order.orderSnapShotProductGroupImages, orderSnapShotProductGroupImage).fetchJoin()
                        .innerJoin(order.productGroupDetailDescription, orderSnapShotProductGroupDetailDescription).fetchJoin()
                        .innerJoin(order.orderSnapShotProductNotice, orderSnapShotProductNotice).fetchJoin()
                        .innerJoin(order.orderSnapShotProductDelivery, orderSnapShotProductDelivery).fetchJoin()
                        .innerJoin(order.orderSnapShotProductStock, orderSnapShotProductStock).fetchJoin()

                        .leftJoin(order.settlement, settlement).fetchJoin()
                        .leftJoin(payment.paymentSnapShotMileage, paymentSnapShotMileage).fetchJoin()
                        .leftJoin(order.orderSnapShotMileage, orderSnapShotMileage).fetchJoin()

                        .where(orderIdEq(orderId), sellerIdEq(sellerIdOpt))
                        .fetchOne()
        );
    }


    @Override
    public List<Order> fetchOrderEntities(List<Long> orderIds, Optional<Long> sellerIdOpt) {
        return getQueryFactory()
                .selectFrom(order)
                .innerJoin(order.shipment, shipment).fetchJoin()
                .innerJoin(order.payment, payment).fetchJoin()
                .innerJoin(payment.paymentBill, paymentBill).fetchJoin()
                .innerJoin(paymentBill.paymentMethod, paymentMethod).fetchJoin()
                .innerJoin(payment.paymentSnapShotShippingAddress, paymentSnapShotShippingAddress).fetchJoin()
                .innerJoin(order.orderSnapShotProduct, orderSnapShotProduct).fetchJoin()
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup).fetchJoin()
                .innerJoin(order.orderSnapShotProductGroupImages, orderSnapShotProductGroupImage).fetchJoin()
                .innerJoin(order.productGroupDetailDescription, orderSnapShotProductGroupDetailDescription).fetchJoin()
                .innerJoin(order.orderSnapShotProductNotice, orderSnapShotProductNotice).fetchJoin()
                .innerJoin(order.orderSnapShotProductDelivery, orderSnapShotProductDelivery).fetchJoin()
                .innerJoin(order.orderSnapShotProductStock, orderSnapShotProductStock).fetchJoin()
                .leftJoin(payment.paymentSnapShotMileage, paymentSnapShotMileage).fetchJoin()
                .leftJoin(order.settlement, settlement).fetchJoin()
                .leftJoin(order.orderSnapShotMileage, orderSnapShotMileage).fetchJoin()
                .where(orderIdIn(orderIds), sellerIdEq(sellerIdOpt))
                .fetch();
    }

    @Override
    public List<OrderHistoryResponse> fetchOrderHistoryEntities(List<Long> orderIds) {
        return getQueryFactory()

            .from(orderHistory)
            .leftJoin(shipment)
                .on(orderHistory.order.id.eq(shipment.order.id))
            .where(orderHistory.order.id.in(orderIds))
            .transform(
                GroupBy.groupBy(orderHistory.id).list(
                    new QOrderHistoryResponse(
                        orderHistory.order.id,
                        orderHistory.changeReason.coalesce(""),
                        orderHistory.changeDetailReason.coalesce(""),
                        orderHistory.orderStatus,
                        shipment.invoiceNo.coalesce(""),
                        shipment.companyCode,
                        orderHistory.insertDate

                    )
                )
            );
    }


    @Override
    public Optional<OrderResponse> fetchOrder(long orderId, Optional<Long> sellerIdOpt){
        return Optional.ofNullable(
                getQueryFactory().from(order)
                        .innerJoin(order.payment, payment)
                        .innerJoin(payment.paymentBill, paymentBill)
                        .innerJoin(paymentBill.paymentMethod, paymentMethod)
                        .innerJoin(order.shipment, shipment)
                        .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                        .innerJoin(order.settlement, settlement)
                        .innerJoin(brand).on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                        .innerJoin(seller).on(seller.id.eq(order.sellerId))
                        .innerJoin(users).on(users.id.eq(order.userId))
                        .innerJoin(payment.paymentSnapShotShippingAddress, paymentSnapShotShippingAddress)
                        .leftJoin(payment.paymentSnapShotMileage, paymentSnapShotMileage)
                        .where(orderIdEq(orderId), sellerIdEq(sellerIdOpt))
                        .transform(
                                GroupBy.groupBy(order.id).as(
                                        new QOrderResponse(
                                                order.id,
                                                paymentBill.buyerInfo,
                                                new QPaymentDetail(
                                                        payment.id,
                                                        paymentBill.paymentAgencyId,
                                                        payment.paymentDetails.paymentStatus,
                                                        paymentMethod.paymentMethodEnum,
                                                        payment.paymentDetails.paymentDate,
                                                        payment.paymentDetails.canceledDate,
                                                        payment.userId,
                                                        payment.paymentDetails.siteName,
                                                        paymentBill.paymentAmount,
                                                        paymentSnapShotMileage.usedMileageAmount.coalesce(BigDecimal.ZERO)

                                                ),
                                                new QReceiverInfo(
                                                        paymentSnapShotShippingAddress.shippingDetails.receiverName,
                                                        paymentSnapShotShippingAddress.shippingDetails.phoneNumber,
                                                        paymentSnapShotShippingAddress.shippingDetails.addressLine1,
                                                        paymentSnapShotShippingAddress.shippingDetails.addressLine2,
                                                        paymentSnapShotShippingAddress.shippingDetails.zipCode,
                                                        paymentSnapShotShippingAddress.shippingDetails.country,
                                                        paymentSnapShotShippingAddress.shippingDetails.deliveryRequest
                                                ),
                                                new QPaymentShipmentInfo(
                                                        shipment.deliveryStatus,
                                                        shipment.companyCode.coalesce(ShipmentCompanyCode.REFER_DETAIL),
                                                        shipment.invoiceNo,
                                                        shipment.deliveryDate
                                                ),
                                                new QSettlementInfo(
                                                        settlement.sellerCommissionRate,
                                                        settlement.settlementDate,
                                                        settlement.directDiscountSellerBurdenRatio,
                                                        order.purchaseConfirmedDate,
                                                        orderSnapShotProductGroup.snapShotProductGroup.price.currentPrice,
                                                        settlement.directDiscountPrice,
                                                        order.quantity
                                                )
                                        )
                                )
                        ).get(orderId)
        );
    }

    @Override
    public List<OrderListResponse> fetchOrders(OrderFilter filter, Pageable pageable) {

        List<Long> orderIds = fetchOrderIds(filter, pageable);
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable);

        return getQueryFactory().from(order)
                .innerJoin(order.payment, payment)
                .innerJoin(order.settlement, settlement)
                .innerJoin(payment.paymentBill, paymentBill)
                .innerJoin(paymentBill.paymentMethod, paymentMethod)
                .innerJoin(order.shipment, shipment)
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                .innerJoin(brand).on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(seller).on(seller.id.eq(order.sellerId))
                .innerJoin(users).on(users.id.eq(order.userId))
                .innerJoin(payment.paymentSnapShotShippingAddress, paymentSnapShotShippingAddress)
                .leftJoin(payment.paymentSnapShotMileage, paymentSnapShotMileage)
                .where(orderIdIn(orderIds))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .distinct()
                .transform(
                        GroupBy.groupBy(order.id).list(
                                new QOrderListResponse(
                                        order.id,
                                        paymentBill.buyerInfo,
                                        new QPaymentDetail(
                                                payment.id,
                                                paymentBill.paymentAgencyId.coalesce(""),
                                                payment.paymentDetails.paymentStatus,
                                                paymentMethod.paymentMethodEnum,
                                                payment.paymentDetails.paymentDate.coalesce(payment.insertDate),
                                                payment.paymentDetails.canceledDate,
                                                payment.userId,
                                                payment.paymentDetails.siteName,
                                                paymentBill.paymentAmount,
                                                paymentSnapShotMileage.usedMileageAmount.coalesce(BigDecimal.ZERO)
                                        ),
                                        new QReceiverInfo(
                                                paymentSnapShotShippingAddress.shippingDetails.receiverName,
                                                paymentSnapShotShippingAddress.shippingDetails.phoneNumber,
                                                paymentSnapShotShippingAddress.shippingDetails.addressLine1,
                                                paymentSnapShotShippingAddress.shippingDetails.addressLine2,
                                                paymentSnapShotShippingAddress.shippingDetails.zipCode,
                                                paymentSnapShotShippingAddress.shippingDetails.country,
                                                paymentSnapShotShippingAddress.shippingDetails.deliveryRequest
                                        ),
                                        new QPaymentShipmentInfo(
                                                shipment.deliveryStatus,
                                                shipment.companyCode.coalesce(ShipmentCompanyCode.REFER_DETAIL),
                                                shipment.invoiceNo.coalesce(""),
                                                shipment.deliveryDate

                                        ),
                                        new QSettlementInfo(
                                                settlement.sellerCommissionRate,
                                                settlement.expectedSettlementDate,
                                                settlement.directDiscountSellerBurdenRatio,
                                                settlement.settlementDate,
                                                orderSnapShotProductGroup.snapShotProductGroup.price.currentPrice,
                                                settlement.directDiscountPrice,
                                                order.quantity
                                        )
                                )
                        )
                );
    }



    private List<Long> fetchOrderIds(OrderFilter filter, Pageable pageable){
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable);

        return getQueryFactory()
                .select(order.id)
                .from(order)
                .innerJoin(order.payment, payment)
                .innerJoin(order.settlement, settlement)
                .innerJoin(payment.paymentBill, paymentBill)
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                .where(
                        betweenTime(filter), orderStatusIn(filter), exclusiveOrderStatus(),
                        sellerIdEq(filter.getSellerId()), searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();
    }


    @Override
    public JPAQuery<Long> fetchOrderCountQuery(OrderFilter filter) {
        return getQueryFactory().select(order.count())
                .from(order)
                .innerJoin(order.payment, payment)
                .innerJoin(order.settlement, settlement)
                .innerJoin(payment.paymentBill, paymentBill)
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)

                .where(
                        betweenTime(filter), orderStatusIn(filter), exclusiveOrderStatus(),
                        sellerIdEq(filter.getSellerId()), searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord())
                )
                .distinct();
    }


    @Override
    public List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds) {
        return getQueryFactory()
                .from(order)
                .innerJoin(users).on(users.id.eq(order.userId))
                .innerJoin(order.payment, payment)
                .innerJoin(order.shipment, shipment)
                .innerJoin(sellerBusinessInfo)
                    .on(sellerBusinessInfo.id.eq(order.sellerId))
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                .leftJoin(order.orderSnapShotMileage, orderSnapShotMileage)
                .leftJoin(orderSnapShotMileageDetail)
                    .on(orderSnapShotMileageDetail.orderSnapShotMileage.id.eq(orderSnapShotMileage.id))
                .leftJoin(vBankAccount)
                    .on(vBankAccount.paymentId.eq(payment.id))
                .where(orderIdIn(orderIds))
                .transform(
                        GroupBy.groupBy(order.id).list(
                                new QProductOrderSheet(
                                        payment.id,
                                        payment.paymentDetails.paymentAmount,
                                        order.id,
                                        order.orderAmount,
                                        orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                                        orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                                        sellerBusinessInfo.csPhoneNumber,
                                        users.phoneNumber.coalesce(""),
                                        GroupBy.list(
                                                orderSnapShotMileageDetail.usedAmount.coalesce(BigDecimal.ZERO)
                                        ),
                                        order.orderStatus,
                                        shipment.companyCode.coalesce(ShipmentCompanyCode.REFER_DETAIL),
                                        shipment.invoiceNo.coalesce(""),
                                        vBankAccount.vBankName,
                                        vBankAccount.vBankNumber,
                                        vBankAccount.vBankHolder,
                                        vBankAccount.paymentAmount,
                                        vBankAccount.vBankDueDate
                                )
                        )
                );
    }

    @Override
    public List<Long> fetchOrderIds(Long lastDomainId) {

        return getQueryFactory()
                .select(order.id)
                .from(order)
                .where(
                        exclusiveOrderStatus(), order.id.gt(lastDomainId),
                        order.orderStatus.notIn(OrderStatus.ORDER_FAILED, OrderStatus.ORDER_PROCESSING, OrderStatus.SALE_CANCELLED_COMPLETED, OrderStatus.CANCEL_REQUEST_COMPLETED,
                                OrderStatus.CANCEL_REQUEST_CONFIRMED, OrderStatus.SALE_CANCELLED, OrderStatus.RETURN_REQUEST_COMPLETED)
                )
                .orderBy(order.id.asc())
                .fetch();
    }


    @Override
    public List<OrderStatistics> fetchOrderMonthStatistics(BaseRoleFilter filter) {

        LocalDate localDate = LocalDate.now();
        LocalDate startOfYear = LocalDate.of(localDate.getYear(), 1, 1);
        LocalDate endOfYear = LocalDate.of(localDate.getYear(), 12, 31);

        StringTemplate monthGroup = getMonthGroupTemplate();


        return getQueryFactory()
                .select(new QOrderStatistics(
                        monthGroup,
                        getTotalOrderCount(),
                        getFilteredOrderCount(),
                        order.orderAmount.sum(),
                        getFilteredOrderAmount()
                ))
                .from(order)
                .where(
                        order.insertDate.between(startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59)),
                        order.orderStatus.notIn(OrderStatus.ORDER_FAILED),
                        sellerIdEq(filter.getSellerId())
                )
                .groupBy(monthGroup)
                .fetch();
    }

    @Override
    public Optional<OrderStatistics> fetchOrderDateStatistics(BaseDateTimeRangeFilter filter) {
        return Optional.ofNullable(getQueryFactory()
                .select(new QOrderStatistics(
                        Expressions.constant("Total"), // 고정된 값으로 한 줄 요약
                        getTotalOrderCount(), // 전체 주문 건수
                        getFilteredOrderCount(), // 필터링된 주문 건수
                        order.orderAmount.sum().coalesce(BigDecimal.ZERO), // 총 주문 금액 합계
                        getFilteredOrderAmount() // 필터링된 주문 금액 합계
                ))
                .from(order)
                .where(
                        order.insertDate.between(filter.getStartDate(), filter.getEndDate()),
                        order.orderStatus.notIn(OrderStatus.ORDER_FAILED),
                        sellerIdEq(filter.getSellerId())
                )
                .fetchOne());
    }


    @Override
    public Optional<OrderTodayCountResponse> fetchOrderTodayCountQuery(BaseRoleFilter filter) {

        List<Tuple> results = getQueryFactory()
            .select(order.orderStatus, order.count())
            .from(order)
            .where(sellerIdEq(filter.getSellerId()), order.orderStatus.in(OrderStatus.ORDER_COMPLETED, OrderStatus.RETURN_REQUEST))
            .groupBy(order.orderStatus)
            .fetch();

        Map<OrderStatus, Long> orderStatusCounts = new HashMap<>();
        for (Tuple result : results) {
            orderStatusCounts.put(result.get(order.orderStatus), result.get(order.count()));
        }

        return Optional.of(
            new OrderTodayCountResponse(
                orderStatusCounts.getOrDefault(OrderStatus.ORDER_COMPLETED, 0L),
                orderStatusCounts.getOrDefault(OrderStatus.RETURN_REQUEST, 0L)
                )
        );
    }

    @Override
    public List<OrderDashboardRanking> fetchOrderDashboardRanking(BaseDateTimeRangeFilter filter) {
        return getQueryFactory()
                .select(new QOrderDashboardRanking(
                        orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                        orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                        getTotalOrderCount(),
                        getFilteredOrderCount()
                ))
                .from(order)
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                .where(
                        sellerIdEq(filter.getSellerId()),
                        order.insertDate.between(filter.getStartDate(), filter.getEndDate()),
                        order.orderStatus.notIn(OrderStatus.ORDER_FAILED)
                )
                .groupBy(
                        orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                        orderSnapShotProductGroup.snapShotProductGroup.productGroupName
                )
                .orderBy(getTotalOrderCount().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<OrderStatistics> fetchOrderExternalStatistics(BaseDateTimeRangeFilter filter) {
        return getQueryFactory()
                .select(new QOrderStatistics(
                        payment.paymentDetails.siteName.stringValue(),
                        getTotalOrderCount(),
                        getFilteredOrderCount(),
                        order.orderAmount.sum(),
                        getFilteredOrderAmount()
                ))
                .from(order)
                .innerJoin(order.payment, payment)
                .where(
                        sellerIdEq(filter.getSellerId()),
                        order.insertDate.between(filter.getStartDate(), filter.getEndDate()),
                        order.orderStatus.notIn(OrderStatus.ORDER_FAILED)
                )
                .groupBy(payment.paymentDetails.siteName.stringValue())
                .fetch();
    }

    @Override
    public List<SettlementResponse> fetchSettlements(BaseDateTimeRangeFilter filter, Pageable pageable) {

        StringTemplate groupDate =  Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                settlement.expectedSettlementDate,
                "%Y-%m-%d"
        );

        StringTemplate settlementCompleteDay =  Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                settlement.settlementDate.max(),
                "%Y-%m-%d"
        );

        NumberExpression<BigDecimal> totalCurrentPrice =
                orderSnapShotProductGroup.snapShotProductGroup.price.currentPrice.multiply(order.quantity);

        NumberExpression<BigDecimal> totalDiscountAmount =
                getCalculateOurMallFlag(settlement.directDiscountPrice.multiply(order.quantity));

        NumberExpression<BigDecimal> sellerDiscountAmount =
                getCalculateOurMallFlag(
                        settlement.directDiscountPrice
                                .multiply(order.quantity)
                                .multiply(settlement.directDiscountSellerBurdenRatio.divide(BigDecimal.valueOf(100)))
                );

        NumberExpression<BigDecimal> totalMileageAmount =
                getCalculateOurMallFlag(settlement.useMileageAmount);

        NumberExpression<BigDecimal> sellerMileageAmount =
                getCalculateOurMallFlag(
                        settlement.useMileageAmount
                                .multiply(settlement.mileageSellerBurdenRatio.divide(BigDecimal.valueOf(100)))
                );

        NumberExpression<BigDecimal> totalFee =
                totalCurrentPrice.multiply(
                        settlement.sellerCommissionRate.divide(BigDecimal.valueOf(100))
                        )
                        .sum()
                        .round();

        return getQueryFactory()
                .select(new QSettlementResponse(
                        getTotalOrderCount(),
                        getOurMallOrderCountCaseWhenQuery(),
                        getExternalMallOrderCountCaseWhenQuery(),
                        groupDate,
                        settlementCompleteDay,
                        totalCurrentPrice.sum(),
                        totalDiscountAmount,
                        sellerDiscountAmount,
                        totalMileageAmount,
                        sellerMileageAmount,
                        totalFee
                ))
                .from(order)
                .innerJoin(order.payment, payment)
                .innerJoin(order.settlement, settlement)
                .innerJoin(order.shipment, shipment)
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                .where(
                        settlement.expectedSettlementDate.between(filter.getStartDate(), filter.getEndDate()),
                        sellerIdEq(filter.getSellerId()),
                        order.orderStatus.in(OrderStatus.SETTLEMENT_COMPLETED, OrderStatus.SETTLEMENT_PROCESSING),
                        shipment.deliveryStatus.eq(DeliveryStatus.DELIVERY_COMPLETED)
                )
                .groupBy(groupDate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    @Override
    public JPAQuery<Long> fetchSettlementCount(BaseDateTimeRangeFilter filter) {
        StringTemplate groupDate =  Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                settlement.expectedSettlementDate,
                "%Y-%m-%d"
        );

        return getQueryFactory()
                .select(groupDate.countDistinct())
                .from(order)
                .innerJoin(order.payment, payment)
                .innerJoin(order.settlement, settlement)
                .innerJoin(order.shipment, shipment)
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                .where(
                        settlement.expectedSettlementDate.between(filter.getStartDate(), filter.getEndDate()),
                        sellerIdEq(filter.getSellerId()),
                        order.orderStatus.in(OrderStatus.SETTLEMENT_COMPLETED, OrderStatus.SETTLEMENT_PROCESSING),
                        shipment.deliveryStatus.eq(DeliveryStatus.DELIVERY_COMPLETED)
                );
    }

    private StringTemplate getMonthGroupTemplate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                order.insertDate,
                "%Y-%m"
        );
    }

    // 전체 주문 건수
    private NumberExpression<Long> getTotalOrderCount() {
        return order.id.count();
    }

    // 필터링된 주문 건수
    private NumberExpression<Long> getFilteredOrderCount() {
        return Expressions.numberTemplate(
                Long.class,
                "sum(case when {0} in ({1}, {2}, {3}, {4}, {5}, {6}, {7}) then 1 else 0 end)",
                order.orderStatus,
                OrderStatus.ORDER_COMPLETED,
                OrderStatus.ORDER_PROCESSING,
                OrderStatus.DELIVERY_COMPLETED,
                OrderStatus.SETTLEMENT_COMPLETED,
                OrderStatus.SETTLEMENT_PROCESSING,
                OrderStatus.DELIVERY_PENDING,
                OrderStatus.DELIVERY_PROCESSING
        ).coalesce(Expressions.constant(0L));
    }

    private NumberExpression<BigDecimal> getFilteredOrderAmount() {
        return new CaseBuilder()
                .when(order.orderStatus.in(
                        OrderStatus.ORDER_COMPLETED,
                        OrderStatus.ORDER_PROCESSING,
                        OrderStatus.DELIVERY_COMPLETED,
                        OrderStatus.SETTLEMENT_COMPLETED,
                        OrderStatus.SETTLEMENT_PROCESSING,
                        OrderStatus.DELIVERY_PENDING,
                        OrderStatus.DELIVERY_PROCESSING
                ))
                .then(order.orderAmount)
                .otherwise(BigDecimal.ZERO)
                .sum().coalesce(Expressions.constant(BigDecimal.ZERO));
    }


    private NumberExpression<Long> getOrderStatusCaseWhenQuery(OrderStatus orderStatus) {
        return Expressions.numberTemplate(
                Long.class,
                "sum(case when {0} = {1} then 1 else 0 end)",
                order.orderStatus,
                orderStatus
        );
    }

    private NumberExpression<Long> getOurMallOrderCountCaseWhenQuery() {
        return Expressions.numberTemplate(
                Long.class,
                "sum(case when {0} = {1} then 1 else 0 end)",
                payment.paymentDetails.siteName,
                SiteName.OUR_MALL
        );
    }

    private NumberExpression<Long> getExternalMallOrderCountCaseWhenQuery() {
        return Expressions.numberTemplate(
                Long.class,
                "sum(case when {0} <> {1} then 1 else 0 end)",
                payment.paymentDetails.siteName,
                SiteName.OUR_MALL
        );
    }

    private NumberExpression<BigDecimal> getCalculateOurMallFlag(NumberExpression<BigDecimal> conditions) {
        return new CaseBuilder()
                .when(payment.paymentDetails.siteName.eq(SiteName.OUR_MALL))
                .then(conditions)
                .otherwise(BigDecimal.ZERO)
                .sum();
    }

    private BooleanExpression orderIdEq(long orderId){
        return order.id.eq(orderId);
    }

    private BooleanExpression orderIdIn(List<Long> orderIds) {
        return order.id.in(orderIds);
    }

    private BooleanExpression exclusiveOrderStatus(){
        return order.orderStatus.notIn(OrderStatus.exclusiveOrderStatus());
    }


    private BooleanExpression orderStatusIn(OrderFilter filterDto){
        if(filterDto.getOrderStatusList() != null){
            if(!filterDto.getOrderStatusList().isEmpty()) return order.orderStatus.in(filterDto.getOrderStatusList());
        }
        return null;
    }

    private BooleanExpression sellerIdEq(Optional<Long> sellerIdOpt){
        return sellerIdOpt.map(order.sellerId::eq).orElse(null);
    }

    private BooleanExpression sellerIdEq(Long sellerId){
        if(sellerId != null) return order.sellerId.eq(sellerId);
        return null;
    }

    private BooleanExpression betweenTime(OrderFilter filter){

        if(filter.isHistory()){
            if(filter.getOrderStatusList() != null){
                if(!filter.getOrderStatusList().isEmpty()) return payment.paymentDetails.canceledDate.between(filter.getStartDate(), filter.getEndDate());
            }
        }

        if(filter.isSettlement()){

            if(filter.getOrderStatusList() != null){
                if(filter.getOrderStatusList().size() ==1 && filter.getOrderStatusList().getFirst().isSettlementCompleted()){
                    return settlement.settlementDate.between(filter.getStartDate(), filter.getEndDate());
                }else{
                    return settlement.expectedSettlementDate.between(filter.getStartDate(), filter.getEndDate());
                }
            }

            return settlement.settlementDate.between(filter.getStartDate(), filter.getEndDate());
        }

        if(filter.getOrderStatusList() != null){
            if(!filter.getOrderStatusList().isEmpty()) return order.insertDate.between(filter.getStartDate(), filter.getEndDate());
        }

        return order.insertDate.between(filter.getStartDate(), filter.getEndDate());
    }



    @Override
    public BooleanExpression searchKeywordEq(SearchKeyword searchKeyword, String searchWord) {
        if(searchKeyword != null){
            if(searchKeyword.isProductGroupId()) {
                return super.searchKeywordEq(SearchKeyword.ORDER_SNAPSHOT_PRODUCT_GROUP_ID, searchWord, orderSnapShotProductGroup);
            }
            return super.searchKeywordEq(searchKeyword, searchWord);
        }
        return null;
    }


    @Override
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> allOrderSpecifiers = super.getAllOrderSpecifiers(pageable);
        if (allOrderSpecifiers.isEmpty()) {
            allOrderSpecifiers.add(order.id.desc());
        }
        return allOrderSpecifiers;
    }

}
