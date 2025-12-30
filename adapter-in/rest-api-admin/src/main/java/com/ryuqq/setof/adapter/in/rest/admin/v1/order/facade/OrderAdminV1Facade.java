package com.ryuqq.setof.adapter.in.rest.admin.v1.order.facade;

import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderListV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.mapper.OrderAdminV1ApiMapper;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.query.GetAdminOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.port.in.query.GetAdminOrdersUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrderUseCase;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentUseCase;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * V1 Order Facade (Admin)
 *
 * <p>여러 V2 UseCase를 조합하여 V1 Order 응답을 구성합니다. Strangler Fig 패턴에 따라 V2 UseCase를 재사용하면서 V1 호환 응답을
 * 제공합니다.
 *
 * <p><strong>조합하는 UseCase:</strong>
 *
 * <ul>
 *   <li>GetOrderUseCase - 주문 정보 조회
 *   <li>GetPaymentUseCase - 결제 정보 조회
 * </ul>
 *
 * <p><strong>TODO: 추후 추가될 UseCase:</strong>
 *
 * <ul>
 *   <li>GetMemberUseCase - 구매자 정보 조회
 *   <li>GetShipmentUseCase - 배송 정보 조회
 *   <li>GetSettlementUseCase - 정산 정보 조회
 *   <li>GetProductSnapshotUseCase - 상품 스냅샷 조회
 *   <li>GetMileageUseCase - 마일리지 사용 정보 조회
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderAdminV1Facade {

    private final GetOrderUseCase getOrderUseCase;
    private final GetAdminOrdersUseCase getAdminOrdersUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final OrderAdminV1ApiMapper orderAdminV1ApiMapper;

    public OrderAdminV1Facade(
            GetOrderUseCase getOrderUseCase,
            GetAdminOrdersUseCase getAdminOrdersUseCase,
            GetPaymentUseCase getPaymentUseCase,
            OrderAdminV1ApiMapper orderAdminV1ApiMapper) {
        this.getOrderUseCase = getOrderUseCase;
        this.getAdminOrdersUseCase = getAdminOrdersUseCase;
        this.getPaymentUseCase = getPaymentUseCase;
        this.orderAdminV1ApiMapper = orderAdminV1ApiMapper;
    }

    /**
     * 주문 상세 조회 (V1 형식)
     *
     * <p>V2 UseCase들을 조합하여 V1 응답 형식으로 변환합니다.
     *
     * @param orderId 주문 ID (UUID String)
     * @return V1 주문 상세 응답
     */
    public OrderV1ApiResponse getOrder(String orderId) {
        OrderResponse orderResponse = getOrderUseCase.getOrder(orderId);

        PaymentResponse paymentResponse = null;
        if (orderResponse.paymentId() != null) {
            paymentResponse = getPaymentUseCaseSafe(orderResponse.paymentId());
        }

        return orderAdminV1ApiMapper.toOrderV1Response(orderResponse, paymentResponse);
    }

    /**
     * 주문 번호로 주문 상세 조회 (V1 형식)
     *
     * @param orderNumber 주문 번호
     * @return V1 주문 상세 응답
     */
    public OrderV1ApiResponse getOrderByOrderNumber(String orderNumber) {
        OrderResponse orderResponse = getOrderUseCase.getOrderByOrderNumber(orderNumber);

        PaymentResponse paymentResponse = null;
        if (orderResponse.paymentId() != null) {
            paymentResponse = getPaymentUseCaseSafe(orderResponse.paymentId());
        }

        return orderAdminV1ApiMapper.toOrderV1Response(orderResponse, paymentResponse);
    }

    /**
     * 결제 정보 안전하게 조회 (실패 시 null 반환)
     *
     * <p>결제 정보가 없거나 조회 실패 시에도 주문 정보는 반환되어야 합니다.
     */
    private PaymentResponse getPaymentUseCaseSafe(String paymentId) {
        try {
            return getPaymentUseCase.getPayment(paymentId);
        } catch (Exception e) {
            // 결제 정보 조회 실패는 주문 조회를 막지 않음
            return null;
        }
    }

    /**
     * Admin 주문 목록 조회 (V1 형식)
     *
     * <p>V2 UseCase를 호출하고 결과를 V1 응답 형식으로 변환합니다.
     *
     * @param query Admin 주문 검색 조건
     * @return V1 주문 목록 Slice 응답
     */
    public SliceResponse<OrderListV1ApiResponse> getOrders(GetAdminOrdersQuery query) {
        SliceResponse<OrderResponse> sliceResponse = getAdminOrdersUseCase.getOrders(query);

        List<OrderListV1ApiResponse> mappedContent =
                sliceResponse.content().stream()
                        .map(orderAdminV1ApiMapper::toOrderListV1Response)
                        .toList();

        return SliceResponse.of(
                mappedContent,
                sliceResponse.size(),
                sliceResponse.hasNext(),
                sliceResponse.nextCursor());
    }
}
