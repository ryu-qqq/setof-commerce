package com.setof.connectly.module.payment.mapper;

import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.dto.OrderRejectReason;
import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import com.setof.connectly.module.payment.dto.refund.RefundPaymentDto;
import com.setof.connectly.module.payment.entity.Payment;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;
import java.util.List;
import java.util.Set;

public interface PaymentMapper {
    PaymentGatewayRequestDto toGeneratePaymentKey(
            long paymentId, List<Long> orderIds, double orderAmounts);

    Payment toEntity(long paymentAmount);

    PaymentBill toPaymentBill(long paymentId, String paymentUniqueId, BasePayment basePayment);

    PaymentSnapShotShippingAddress toSnapShotShippingAddress(
            long paymentId, BasePayment basePayment);

    RefundPaymentDto toRefundResult(long paymentId, RefundOrderSheet refundOrderSheet);

    PaymentResponse toPaymentResponse(
            PaymentResponse paymentResponse, Set<OrderProductDto> options);

    PaymentResponse toPaymentResponse(
            PaymentResponse paymentResponse,
            Set<OrderProductDto> options,
            List<OrderRejectReason> orderRejectReasons);

    List<PaymentResponse> toPaymentResponses(
            List<PaymentResponse> paymentResponses,
            Set<OrderProductDto> options,
            List<OrderRejectReason> orderRejectReasons);
}
