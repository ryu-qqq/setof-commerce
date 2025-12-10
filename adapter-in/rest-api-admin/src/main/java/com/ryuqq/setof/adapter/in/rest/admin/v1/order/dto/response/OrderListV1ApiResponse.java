package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 주문 목록 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 목록 응답")
public record OrderListV1ApiResponse(
        @Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "구매자 정보") OrderV1ApiResponse.BuyerInfoV1ApiResponse buyerInfo,
        @Schema(description = "결제 상세 정보") OrderV1ApiResponse.PaymentDetailV1ApiResponse payment,
        @Schema(description = "수령인 정보") OrderV1ApiResponse.ReceiverInfoV1ApiResponse receiverInfo,
        @Schema(description = "배송 정보")
                OrderV1ApiResponse.PaymentShipmentInfoV1ApiResponse paymentShipmentInfo,
        @Schema(description = "정산 정보")
                OrderV1ApiResponse.SettlementInfoV1ApiResponse settlementInfo,
        @Schema(description = "주문 상품 정보") OrderV1ApiResponse.OrderProductV1ApiResponse orderProduct,
        @Schema(description = "주문 히스토리 목록") List<OrderHistoryV1ApiResponse> orderHistories) {}
