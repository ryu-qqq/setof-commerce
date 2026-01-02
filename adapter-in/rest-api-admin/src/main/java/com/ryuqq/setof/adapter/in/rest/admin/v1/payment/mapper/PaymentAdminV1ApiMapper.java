package com.ryuqq.setof.adapter.in.rest.admin.v1.payment.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.payment.dto.response.PaymentAdminV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.payment.dto.response.PaymentAdminV1ApiResponse.BuyerInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.payment.dto.response.PaymentAdminV1ApiResponse.ReceiverInfoV1ApiResponse;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * V1 Payment Admin API Mapper
 *
 * <p>V2 Application Response를 V1 Admin API Response로 변환합니다. Strangler Fig 패턴에 따라 V2 UseCase를 재사용하면서
 * V1 호환 응답을 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentAdminV1ApiMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /**
     * PaymentResponse + OrderResponse를 PaymentAdminV1ApiResponse로 변환
     *
     * @param paymentResponse V2 결제 응답
     * @param orderResponse V2 주문 응답 (nullable)
     * @return V1 Admin 결제 응답
     */
    public PaymentAdminV1ApiResponse toPaymentAdminV1Response(
            PaymentResponse paymentResponse, OrderResponse orderResponse) {

        return new PaymentAdminV1ApiResponse(
                extractLegacyPaymentId(paymentResponse.paymentId()),
                orderResponse != null ? extractLegacyOrderId(orderResponse.orderId()) : null,
                paymentResponse.pgTransactionId(),
                paymentResponse.status(),
                paymentResponse.method(),
                getPaymentMethodDisplayName(paymentResponse.method()),
                toLong(paymentResponse.requestedAmount()),
                toLong(paymentResponse.approvedAmount()),
                toLocalDateTime(paymentResponse.approvedAt()),
                toLocalDateTime(paymentResponse.cancelledAt()),
                orderResponse != null ? orderResponse.sellerId() : null,
                orderResponse != null ? extractLegacyMemberId(orderResponse.memberId()) : null,
                toBuyerInfo(orderResponse),
                toReceiverInfo(orderResponse));
    }

    /**
     * PaymentResponse만으로 변환 (주문 정보 없이)
     *
     * @param paymentResponse V2 결제 응답
     * @return V1 Admin 결제 응답
     */
    public PaymentAdminV1ApiResponse toPaymentAdminV1Response(PaymentResponse paymentResponse) {
        return toPaymentAdminV1Response(paymentResponse, null);
    }

    private BuyerInfoV1ApiResponse toBuyerInfo(OrderResponse orderResponse) {
        // TODO: GetMemberUseCase로 memberId 기반 조회 필요
        return new BuyerInfoV1ApiResponse(
                null, // TODO: member.getName()
                null, // TODO: member.getEmail()
                null // TODO: member.getPhoneNumber()
                );
    }

    private ReceiverInfoV1ApiResponse toReceiverInfo(OrderResponse orderResponse) {
        if (orderResponse == null) {
            return null;
        }

        return new ReceiverInfoV1ApiResponse(
                orderResponse.receiverName(),
                orderResponse.receiverPhone(),
                orderResponse.address(),
                orderResponse.addressDetail(),
                orderResponse.zipCode(),
                orderResponse.memo());
    }

    private String getPaymentMethodDisplayName(String method) {
        if (method == null) {
            return null;
        }
        return switch (method) {
            case "CARD" -> "카드";
            case "VBANK" -> "가상계좌";
            case "TRANSFER" -> "계좌이체";
            case "PHONE" -> "휴대폰";
            default -> method;
        };
    }

    private Long extractLegacyPaymentId(String paymentId) {
        if (paymentId == null) {
            return null;
        }
        // TODO: UUID → Long 매핑 테이블 도입 검토
        return (long) Math.abs(paymentId.hashCode());
    }

    private Long extractLegacyOrderId(String orderId) {
        if (orderId == null) {
            return null;
        }
        return (long) Math.abs(orderId.hashCode());
    }

    private Long extractLegacyMemberId(String memberId) {
        if (memberId == null) {
            return null;
        }
        return (long) Math.abs(memberId.hashCode());
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, KST);
    }

    private Long toLong(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.longValue();
    }
}
