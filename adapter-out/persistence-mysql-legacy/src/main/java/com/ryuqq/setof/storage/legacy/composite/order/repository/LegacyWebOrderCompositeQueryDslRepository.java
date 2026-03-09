package com.ryuqq.setof.storage.legacy.composite.order.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderHistoryEntity.legacyOrderHistoryEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotOptionDetailEntity.legacyOrderSnapshotOptionDetailEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotOptionGroupEntity.legacyOrderSnapshotOptionGroupEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotProductDeliveryEntity.legacyOrderSnapshotProductDeliveryEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotProductGroupEntity.legacyOrderSnapshotProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotProductGroupImageEntity.legacyOrderSnapshotProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotProductOptionEntity.legacyOrderSnapshotProductOptionEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentBillEntity.legacyPaymentBillEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentEntity.legacyPaymentEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentSnapshotShippingAddressEntity.legacyPaymentSnapshotShippingAddressEntity;
import static com.ryuqq.setof.storage.legacy.paymentmethod.entity.QLegacyPaymentMethodEntity.legacyPaymentMethodEntity;
import static com.ryuqq.setof.storage.legacy.shipment.entity.QLegacyShipmentEntity.legacyShipmentEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.legacy.order.dto.query.LegacyOrderSearchCondition;
import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.order.condition.LegacyWebOrderCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderDetailFlatDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderHistoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderOptionFlatDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderQueryDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderStatusCountDto;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderSnapshotProductGroupImageEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebOrderCompositeQueryDslRepository - 레거시 주문 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebOrderCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebOrderCompositeConditionBuilder conditionBuilder;

    public LegacyWebOrderCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebOrderCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 주문 목록 조회.
     *
     * <p>커서 기반 페이징, 날짜 범위, 상태 필터 지원.
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 주문 목록
     */
    public List<LegacyWebOrderQueryDto> fetchOrders(
            LegacyOrderSearchCondition condition, int limit) {
        return queryFactory
                .select(createProjection())
                .from(legacyOrderEntity)
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.orderIdLt(condition.lastDomainId()),
                        conditionBuilder.insertDateBetween(
                                condition.startDate(), condition.endDate()),
                        conditionBuilder.orderStatusIn(condition.orderStatusList()))
                .orderBy(legacyOrderEntity.id.desc())
                .limit(limit)
                .distinct()
                .fetch();
    }

    /**
     * 주문 ID 목록 조회 (커서 기반 페이징용).
     *
     * @param criteria 검색 조건
     * @return 주문 ID 목록
     */
    public List<Long> fetchOrderIds(OrderSearchCriteria criteria) {
        DateRange dateRange = criteria.dateRange();
        LocalDateTime startDateTime =
                dateRange != null && dateRange.startDate() != null
                        ? dateRange.startDate().atStartOfDay()
                        : null;
        LocalDateTime endDateTime =
                dateRange != null && dateRange.endDate() != null
                        ? dateRange.endDate().plusDays(1).atStartOfDay().minusNanos(1)
                        : null;

        return queryFactory
                .select(legacyOrderEntity.id)
                .from(legacyOrderEntity)
                .where(
                        conditionBuilder.userIdEq(criteria.userId()),
                        conditionBuilder.orderIdLt(criteria.cursor()),
                        conditionBuilder.insertDateBetween(startDateTime, endDateTime),
                        conditionBuilder.orderStatusIn(criteria.orderStatuses()))
                .orderBy(legacyOrderEntity.id.desc())
                .limit(criteria.fetchSize())
                .distinct()
                .fetch();
    }

    /**
     * 주문 개수 조회.
     *
     * @param condition 검색 조건
     * @return 주문 개수
     */
    public long countOrders(LegacyOrderSearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyOrderEntity.count())
                        .from(legacyOrderEntity)
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.orderIdLt(condition.lastDomainId()),
                                conditionBuilder.insertDateBetween(
                                        condition.startDate(), condition.endDate()),
                                conditionBuilder.orderStatusIn(condition.orderStatusList()))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 상태별 주문 개수 조회 (최근 3개월).
     *
     * @param userId 사용자 ID
     * @param orderStatuses 주문 상태 목록
     * @return 상태별 주문 개수 목록
     */
    public List<LegacyWebOrderStatusCountDto> countOrdersByStatus(
            long userId, List<String> orderStatuses) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebOrderStatusCountDto.class,
                                legacyOrderEntity.orderStatus.stringValue(),
                                legacyOrderEntity.count()))
                .from(legacyOrderEntity)
                .where(
                        conditionBuilder.userIdEq(userId),
                        conditionBuilder.orderStatusIn(orderStatuses),
                        conditionBuilder.updateDateAfter3Months())
                .groupBy(legacyOrderEntity.orderStatus)
                .fetch();
    }

    /**
     * 주문 이력 조회 (orders_history + shipment LEFT JOIN).
     *
     * @param orderId 주문 ID
     * @return 주문 이력 목록
     */
    public List<LegacyWebOrderHistoryQueryDto> fetchOrderHistories(long orderId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebOrderHistoryQueryDto.class,
                                legacyOrderHistoryEntity.orderId,
                                legacyOrderHistoryEntity.changeReason.coalesce(""),
                                legacyOrderHistoryEntity.changeDetailReason.coalesce(""),
                                legacyOrderHistoryEntity.orderStatus.stringValue(),
                                legacyShipmentEntity.invoiceNo.coalesce(""),
                                legacyShipmentEntity.companyCode.stringValue().coalesce(""),
                                legacyOrderHistoryEntity.updateDate))
                .from(legacyOrderHistoryEntity)
                .leftJoin(legacyShipmentEntity)
                .on(legacyShipmentEntity.orderId.eq(legacyOrderHistoryEntity.orderId))
                .where(legacyOrderHistoryEntity.orderId.eq(orderId))
                .orderBy(legacyOrderHistoryEntity.updateDate.asc())
                .fetch();
    }

    /**
     * 주문 상세 조회 (다중 JOIN).
     *
     * <p>orders + payment + payment_bill + payment_method + shipment + order_snapshot_product_group
     * + order_snapshot_product_group_image (MAIN) + brand + payment_snapshot_shipping_address +
     * order_snapshot_product_delivery.
     *
     * @param orderIds 주문 ID 목록
     * @return 주문 상세 flat DTO 목록
     */
    public List<LegacyWebOrderDetailFlatDto> fetchOrderDetails(List<Long> orderIds) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebOrderDetailFlatDto.class,
                                // orders
                                legacyOrderEntity.id,
                                legacyOrderEntity.paymentId,
                                legacyOrderEntity.productId,
                                legacyOrderEntity.sellerId,
                                legacyOrderEntity.userId,
                                legacyOrderEntity.orderAmount,
                                legacyOrderEntity.orderStatus.stringValue(),
                                legacyOrderEntity.quantity,
                                legacyOrderEntity.reviewYn.stringValue(),
                                legacyOrderEntity.insertDate,
                                // payment
                                legacyPaymentEntity.paymentStatus.stringValue(),
                                legacyPaymentEntity.paymentDate,
                                legacyPaymentEntity.canceledDate,
                                // payment_bill
                                legacyPaymentBillEntity.paymentAgencyId.coalesce(""),
                                legacyPaymentBillEntity.paymentAmount,
                                legacyPaymentBillEntity.usedMileageAmount,
                                legacyPaymentBillEntity.buyerName.coalesce(""),
                                legacyPaymentBillEntity.buyerEmail.coalesce(""),
                                legacyPaymentBillEntity.buyerPhoneNumber.coalesce(""),
                                legacyPaymentBillEntity.cardName.coalesce(""),
                                legacyPaymentBillEntity.cardNumber.coalesce(""),
                                // payment_method
                                legacyPaymentMethodEntity.paymentMethod.coalesce(""),
                                // shipment
                                legacyShipmentEntity.deliveryStatus.stringValue(),
                                legacyShipmentEntity.companyCode.stringValue(),
                                legacyShipmentEntity.invoiceNo.coalesce(""),
                                legacyShipmentEntity.insertDate,
                                // order_snapshot_product_group
                                legacyOrderSnapshotProductGroupEntity.productGroupId,
                                legacyOrderSnapshotProductGroupEntity.productGroupName,
                                legacyOrderSnapshotProductGroupEntity.brandId,
                                legacyOrderSnapshotProductGroupEntity.regularPrice,
                                legacyOrderSnapshotProductGroupEntity.salePrice,
                                legacyOrderSnapshotProductGroupEntity.directDiscountPrice,
                                // order_snapshot_product_group_image (MAIN)
                                legacyOrderSnapshotProductGroupImageEntity.imageUrl.coalesce(""),
                                // brand
                                legacyBrandEntity.brandName.coalesce(""),
                                // payment_snapshot_shipping_address
                                legacyPaymentSnapshotShippingAddressEntity.receiverName.coalesce(
                                        ""),
                                legacyPaymentSnapshotShippingAddressEntity.addressLine1.coalesce(
                                        ""),
                                legacyPaymentSnapshotShippingAddressEntity.addressLine2.coalesce(
                                        ""),
                                legacyPaymentSnapshotShippingAddressEntity.phoneNumber.coalesce(""),
                                legacyPaymentSnapshotShippingAddressEntity.zipCode.coalesce(""),
                                legacyPaymentSnapshotShippingAddressEntity.country.coalesce(""),
                                legacyPaymentSnapshotShippingAddressEntity.deliveryRequest.coalesce(
                                        ""),
                                // order_snapshot_product_delivery
                                legacyOrderSnapshotProductDeliveryEntity.returnMethodDomestic
                                        .coalesce(""),
                                legacyOrderSnapshotProductDeliveryEntity.returnCourierDomestic
                                        .coalesce(""),
                                legacyOrderSnapshotProductDeliveryEntity.returnChargeDomestic,
                                legacyOrderSnapshotProductDeliveryEntity.returnExchangeAreaDomestic
                                        .coalesce("")))
                .from(legacyOrderEntity)
                .innerJoin(legacyPaymentEntity)
                .on(legacyPaymentEntity.id.eq(legacyOrderEntity.paymentId))
                .innerJoin(legacyPaymentBillEntity)
                .on(legacyPaymentBillEntity.paymentId.eq(legacyPaymentEntity.id))
                .leftJoin(legacyPaymentMethodEntity)
                .on(legacyPaymentMethodEntity.id.eq(legacyPaymentBillEntity.paymentMethodId))
                .innerJoin(legacyShipmentEntity)
                .on(legacyShipmentEntity.orderId.eq(legacyOrderEntity.id))
                .innerJoin(legacyOrderSnapshotProductGroupEntity)
                .on(legacyOrderSnapshotProductGroupEntity.orderId.eq(legacyOrderEntity.id))
                .leftJoin(legacyOrderSnapshotProductGroupImageEntity)
                .on(
                        legacyOrderSnapshotProductGroupImageEntity.orderId.eq(legacyOrderEntity.id),
                        legacyOrderSnapshotProductGroupImageEntity.productGroupImageType.eq(
                                LegacyOrderSnapshotProductGroupImageEntity.ProductGroupImageType
                                        .MAIN))
                .innerJoin(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyOrderSnapshotProductGroupEntity.brandId))
                .leftJoin(legacyPaymentSnapshotShippingAddressEntity)
                .on(
                        legacyPaymentSnapshotShippingAddressEntity.paymentId.eq(
                                legacyOrderEntity.paymentId))
                .leftJoin(legacyOrderSnapshotProductDeliveryEntity)
                .on(legacyOrderSnapshotProductDeliveryEntity.orderId.eq(legacyOrderEntity.id))
                .where(conditionBuilder.orderIdIn(orderIds))
                .orderBy(legacyOrderEntity.id.desc())
                .fetch();
    }

    /**
     * 주문별 옵션 스냅샷 조회.
     *
     * <p>order_snapshot_product_option + order_snapshot_option_group + order_snapshot_option_detail
     * JOIN.
     *
     * @param orderIds 주문 ID 목록
     * @return 옵션 flat DTO 목록
     */
    public List<LegacyWebOrderOptionFlatDto> fetchOrderOptions(List<Long> orderIds) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebOrderOptionFlatDto.class,
                                legacyOrderSnapshotProductOptionEntity.orderId,
                                legacyOrderSnapshotProductOptionEntity.optionGroupId,
                                legacyOrderSnapshotProductOptionEntity.optionDetailId,
                                legacyOrderSnapshotOptionGroupEntity.optionName.coalesce(""),
                                legacyOrderSnapshotOptionDetailEntity.optionValue.coalesce("")))
                .from(legacyOrderSnapshotProductOptionEntity)
                .innerJoin(legacyOrderSnapshotOptionGroupEntity)
                .on(
                        legacyOrderSnapshotOptionGroupEntity.orderId.eq(
                                legacyOrderSnapshotProductOptionEntity.orderId),
                        legacyOrderSnapshotOptionGroupEntity.optionGroupId.eq(
                                legacyOrderSnapshotProductOptionEntity.optionGroupId))
                .innerJoin(legacyOrderSnapshotOptionDetailEntity)
                .on(
                        legacyOrderSnapshotOptionDetailEntity.orderId.eq(
                                legacyOrderSnapshotProductOptionEntity.orderId),
                        legacyOrderSnapshotOptionDetailEntity.optionDetailId.eq(
                                legacyOrderSnapshotProductOptionEntity.optionDetailId))
                .where(legacyOrderSnapshotProductOptionEntity.orderId.in(orderIds))
                .fetch();
    }

    /**
     * Projections.constructor()로 Projection 생성.
     *
     * <p>@QueryProjection 대신 사용.
     */
    private com.querydsl.core.types.ConstructorExpression<LegacyWebOrderQueryDto>
            createProjection() {
        return Projections.constructor(
                LegacyWebOrderQueryDto.class,
                legacyOrderEntity.id,
                legacyOrderEntity.paymentId,
                legacyOrderEntity.productId,
                legacyOrderEntity.sellerId,
                legacyOrderEntity.userId,
                legacyOrderEntity.orderAmount,
                legacyOrderEntity.orderStatus.stringValue(),
                legacyOrderEntity.quantity,
                legacyOrderEntity.reviewYn.stringValue(),
                legacyOrderEntity.insertDate,
                legacyOrderEntity.updateDate);
    }
}
