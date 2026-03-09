package com.ryuqq.setof.storage.legacy.composite.order.adapter;

import com.ryuqq.setof.application.legacy.order.dto.response.LegacyOrderResult;
import com.ryuqq.setof.application.order.port.out.query.OrderCompositeQueryPort;
import com.ryuqq.setof.application.order.port.out.query.OrderDetailQueryPort;
import com.ryuqq.setof.application.order.port.out.query.OrderHistoryQueryPort;
import com.ryuqq.setof.application.order.port.out.query.OrderStatusCountQueryPort;
import com.ryuqq.setof.domain.legacy.order.dto.query.LegacyOrderSearchCondition;
import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OptionSnapshot;
import com.ryuqq.setof.domain.order.vo.OrderBuyerInfo;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.order.vo.OrderHistory;
import com.ryuqq.setof.domain.order.vo.OrderPaymentSummary;
import com.ryuqq.setof.domain.order.vo.OrderProductSnapshot;
import com.ryuqq.setof.domain.order.vo.OrderRefundNotice;
import com.ryuqq.setof.domain.order.vo.OrderShipmentSummary;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderDetailFlatDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderHistoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderOptionFlatDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderQueryDto;
import com.ryuqq.setof.storage.legacy.composite.order.dto.LegacyWebOrderStatusCountDto;
import com.ryuqq.setof.storage.legacy.composite.order.mapper.LegacyWebOrderMapper;
import com.ryuqq.setof.storage.legacy.composite.order.repository.LegacyWebOrderCompositeQueryDslRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebOrderCompositeQueryAdapter - 레거시 주문 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebOrderCompositeQueryAdapter
        implements OrderStatusCountQueryPort,
                OrderHistoryQueryPort,
                OrderDetailQueryPort,
                OrderCompositeQueryPort {

    private final LegacyWebOrderCompositeQueryDslRepository repository;
    private final LegacyWebOrderMapper mapper;

    public LegacyWebOrderCompositeQueryAdapter(
            LegacyWebOrderCompositeQueryDslRepository repository, LegacyWebOrderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 주문 목록 조회.
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 주문 목록
     */
    public List<LegacyOrderResult> fetchOrders(LegacyOrderSearchCondition condition, int limit) {
        List<LegacyWebOrderQueryDto> dtos = repository.fetchOrders(condition, limit);
        return mapper.toResults(dtos);
    }

    /**
     * 주문 ID 목록 조회 (커서 기반 페이징용).
     *
     * @param criteria 검색 조건
     * @return 주문 ID 목록
     */
    @Override
    public List<Long> fetchOrderIds(OrderSearchCriteria criteria) {
        return repository.fetchOrderIds(criteria);
    }

    /**
     * 주문 개수 조회.
     *
     * @param condition 검색 조건
     * @return 주문 개수
     */
    public long countOrders(LegacyOrderSearchCondition condition) {
        return repository.countOrders(condition);
    }

    /**
     * 상태별 주문 개수 조회 (최근 3개월).
     *
     * @param userId 사용자 ID
     * @param orderStatuses 주문 상태 목록
     * @return 상태별 주문 개수 목록
     */
    @Override
    public List<OrderStatusCount> countByStatus(long userId, List<String> orderStatuses) {
        List<LegacyWebOrderStatusCountDto> dtos =
                repository.countOrdersByStatus(userId, orderStatuses);
        return dtos.stream()
                .map(dto -> OrderStatusCount.of(dto.orderStatus(), dto.count()))
                .toList();
    }

    /**
     * 주문 이력 조회.
     *
     * @param orderId 주문 ID
     * @return 주문 이력 목록 (도메인 VO)
     */
    @Override
    public List<OrderHistory> findByOrderId(long orderId) {
        List<LegacyWebOrderHistoryQueryDto> dtos = repository.fetchOrderHistories(orderId);
        return dtos.stream()
                .map(
                        dto ->
                                OrderHistory.of(
                                        dto.orderId(),
                                        dto.changeReason(),
                                        dto.changeDetailReason(),
                                        dto.orderStatus(),
                                        dto.invoiceNo(),
                                        dto.shipmentCompanyCode(),
                                        dto.updateDate()))
                .toList();
    }

    /**
     * 주문 ID 목록으로 주문 상세 Composite 조회.
     *
     * <p>Step 1: 메인 JOIN 쿼리로 기본 정보 조회. Step 2: 옵션 스냅샷 별도 조회. Step 3: 옵션을 orderId 기준으로 그룹핑하여 도메인 VO
     * 조립.
     *
     * @param orderIds 주문 ID 목록
     * @return 주문 상세 목록 (도메인 VO)
     */
    @Override
    public List<OrderDetail> fetchOrderDetails(List<Long> orderIds) {
        if (orderIds.isEmpty()) {
            return List.of();
        }

        List<LegacyWebOrderDetailFlatDto> detailDtos = repository.fetchOrderDetails(orderIds);
        List<LegacyWebOrderOptionFlatDto> optionDtos = repository.fetchOrderOptions(orderIds);

        Map<Long, List<OptionSnapshot>> optionsByOrderId =
                optionDtos.stream()
                        .collect(
                                Collectors.groupingBy(
                                        LegacyWebOrderOptionFlatDto::orderId,
                                        Collectors.mapping(
                                                dto ->
                                                        OptionSnapshot.of(
                                                                dto.optionGroupId(),
                                                                dto.optionDetailId(),
                                                                dto.optionName(),
                                                                dto.optionValue()),
                                                Collectors.toList())));

        return detailDtos.stream().map(dto -> assembleOrderDetail(dto, optionsByOrderId)).toList();
    }

    private OrderDetail assembleOrderDetail(
            LegacyWebOrderDetailFlatDto dto, Map<Long, List<OptionSnapshot>> optionsByOrderId) {

        List<OptionSnapshot> options = optionsByOrderId.getOrDefault(dto.orderId(), List.of());

        OrderProductSnapshot productSnapshot =
                new OrderProductSnapshot(
                        dto.productGroupId(),
                        dto.productGroupName(),
                        dto.productId(),
                        dto.sellerId(),
                        "",
                        dto.brandId(),
                        dto.brandName(),
                        0,
                        dto.mainImageUrl(),
                        options);

        OrderPaymentSummary paymentSummary =
                OrderPaymentSummary.of(
                        dto.paymentId(),
                        dto.paymentAgencyId(),
                        dto.paymentStatus(),
                        dto.paymentMethod(),
                        dto.paymentDate(),
                        dto.canceledDate(),
                        dto.billPaymentAmount(),
                        dto.usedMileageAmount(),
                        dto.cardName(),
                        dto.cardNumber());

        OrderBuyerInfo buyerInfo =
                OrderBuyerInfo.of(dto.buyerName(), dto.buyerPhoneNumber(), dto.buyerEmail());

        ReceiverInfo receiverInfo =
                ReceiverInfo.of(
                        dto.receiverName(),
                        dto.receiverPhoneNumber(),
                        dto.addressLine1(),
                        dto.addressLine2(),
                        dto.zipCode(),
                        dto.country(),
                        dto.deliveryRequest());

        OrderShipmentSummary shipmentSummary =
                OrderShipmentSummary.of(
                        dto.orderId(),
                        dto.deliveryStatus(),
                        dto.companyCode(),
                        dto.invoiceNo(),
                        dto.shipmentInsertDate());

        OrderRefundNotice refundNotice =
                OrderRefundNotice.of(
                        dto.returnMethodDomestic(),
                        dto.returnCourierDomestic(),
                        dto.returnChargeDomestic(),
                        dto.returnExchangeAreaDomestic());

        return new OrderDetail(
                dto.orderId(),
                dto.orderStatus(),
                dto.quantity(),
                dto.orderAmount(),
                dto.reviewYn(),
                dto.insertDate(),
                productSnapshot,
                dto.regularPrice(),
                dto.salePrice(),
                dto.directDiscountPrice(),
                paymentSummary,
                buyerInfo,
                receiverInfo,
                shipmentSummary,
                refundNotice,
                0);
    }
}
