package com.setof.connectly.module.payment.mapper;

import com.setof.connectly.auth.dto.UserPrincipal;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.dto.OrderRejectReason;
import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import com.setof.connectly.module.payment.dto.refund.RefundPaymentDto;
import com.setof.connectly.module.payment.entity.Payment;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.payment.entity.embedded.PaymentDetails;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;
import com.setof.connectly.module.payment.enums.PaymentChannel;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import com.setof.connectly.module.user.enums.SiteName;
import com.setof.connectly.module.utils.SecurityUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {
    private static final String PAYMENT_KEY = "PAYMENT";
    private static final String DATE_FORMAT = "yyyyMMdd";

    @Override
    public PaymentGatewayRequestDto toGeneratePaymentKey(
            long paymentId, List<Long> orderIds, double orderAmounts) {

        String todayDate = formatDate(LocalDate.now());
        String paymentUniqueId = PAYMENT_KEY + todayDate + "_" + paymentId;

        double expectedMileageAmount = Math.ceil(0.01 * orderAmounts * 100) / 100;

        return PaymentGatewayRequestDto.builder()
                .paymentId(paymentId)
                .orderIds(orderIds)
                .paymentUniqueId(paymentUniqueId)
                .expectedMileageAmount(expectedMileageAmount)
                .build();
    }

    @Override
    public Payment toEntity(long paymentAmount) {
        return Payment.builder().paymentDetails(toPaymentDetails(paymentAmount)).build();
    }

    @Override
    public PaymentBill toPaymentBill(
            long paymentId, String paymentUniqueId, BasePayment basePayment) {
        UserPrincipal authentication = SecurityUtils.getAuthentication();

        return PaymentBill.builder()
                .paymentId(paymentId)
                .userId(SecurityUtils.currentUserId())
                .paymentMethodId(basePayment.getPayMethod().getPaymentMethodId())
                .usedMileageAmount(basePayment.getMileageAmount())
                .paymentChannel(PaymentChannel.PC)
                .paymentUniqueId(paymentUniqueId)
                .buyerInfo(
                        new BuyerInfo(
                                authentication.getName(), "", authentication.getPhoneNumber()))
                .paymentAmount(basePayment.getPayAmount())
                .build();
    }

    @Override
    public PaymentSnapShotShippingAddress toSnapShotShippingAddress(
            long paymentId, BasePayment basePayment) {

        return PaymentSnapShotShippingAddress.builder()
                .paymentId(paymentId)
                .shippingDetails(basePayment.getShippingInfo().getShippingDetails())
                .build();
    }

    @Override
    public RefundPaymentDto toRefundResult(long paymentId, RefundOrderSheet refundOrderSheet) {
        return RefundPaymentDto.builder()
                .paymentId(paymentId)
                .orderId(refundOrderSheet.getOrderId())
                .refundAmount(refundOrderSheet.getExpectedRefundAmount())
                .refundMileageAmount(refundOrderSheet.getExpectedRefundMileage())
                .orderStatus(refundOrderSheet.getOrderStatus())
                .build();
    }

    @Override
    public PaymentResponse toPaymentResponse(
            PaymentResponse paymentResponse, Set<OrderProductDto> options) {
        double usedMileageAmount = paymentResponse.getPayment().getUsedMileageAmount();
        long salePriceAmount = paymentResponse.getSalePriceAmount();
        options.forEach(
                orderProductDto -> {
                    orderProductDto.setOption();
                    orderProductDto.setTotalExpectedRefundMileageAmount(
                            salePriceAmount, usedMileageAmount);
                });
        paymentResponse.setOrderProducts(options);
        paymentResponse.setPreDiscountAmount();
        return paymentResponse;
    }

    @Override
    public PaymentResponse toPaymentResponse(
            PaymentResponse paymentResponse,
            Set<OrderProductDto> options,
            List<OrderRejectReason> orderRejectReasons) {
        if (!orderRejectReasons.isEmpty()) {
            setRejectedOrder(options, orderRejectReasons);
        }
        return toPaymentResponse(paymentResponse, options);
    }

    private void setRejectedOrder(
            Set<OrderProductDto> options, List<OrderRejectReason> orderRejectReasons) {
        Map<Long, Map<OrderStatus, OrderRejectReason>> rejectReasonMap =
                orderRejectReasons.stream()
                        .collect(
                                Collectors.groupingBy(
                                        OrderRejectReason::getOrderId,
                                        Collectors.toMap(
                                                OrderRejectReason::getOrderStatus,
                                                Function.identity(),
                                                (r1, r2) -> r1)));

        for (OrderProductDto product : options) {
            Map<OrderStatus, OrderRejectReason> rejectReasons =
                    rejectReasonMap.get(product.getOrderId());
            if (rejectReasons != null) {
                OrderRejectReason rejectReason = null;

                for (OrderStatus status : OrderStatus.getRejectedOrderStatus()) {
                    if (rejectReasons.containsKey(status)) {
                        rejectReason = rejectReasons.get(status);
                        break;
                    }
                }

                if (rejectReason != null) {
                    LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
                    if (rejectReason.getInsertDate().isAfter(thirtyDaysAgo)
                            || rejectReason.getInsertDate().isEqual(thirtyDaysAgo)) {
                        product.setOrderRejectReason(rejectReason);
                        product.setClaimRejected(Yn.Y);
                    }
                } else {
                    product.setClaimRejected(Yn.N);
                }

            } else {
                product.setClaimRejected(Yn.N);
            }
        }
    }

    @Override
    public List<PaymentResponse> toPaymentResponses(
            List<PaymentResponse> paymentResponses,
            Set<OrderProductDto> options,
            List<OrderRejectReason> orderRejectReasons) {

        if (!orderRejectReasons.isEmpty()) {
            setRejectedOrder(options, orderRejectReasons);
        }

        Map<Long, List<OrderProductDto>> orderProductMap =
                options.stream()
                        .map(OrderProductDto::setOption)
                        .collect(Collectors.groupingBy(OrderProductDto::getPaymentId));

        for (PaymentResponse paymentResponse : paymentResponses) {
            Long paymentId = paymentResponse.getPayment().getPaymentId();
            double usedMileageAmount = paymentResponse.getPayment().getUsedMileageAmount();
            long salePriceAmount = paymentResponse.getSalePriceAmount();
            List<OrderProductDto> orderProductDtos =
                    orderProductMap.getOrDefault(paymentId, Collections.emptyList());
            orderProductDtos.forEach(
                    orderProductDto -> {
                        orderProductDto.setOption();
                        orderProductDto.setTotalExpectedRefundMileageAmount(
                                salePriceAmount, usedMileageAmount);
                    });
            paymentResponse.setOrderProducts(new HashSet<>(orderProductDtos));
            paymentResponse.setPreDiscountAmount();
        }

        return paymentResponses;
    }

    private PaymentDetails toPaymentDetails(long paymentAmount) {
        PaymentDetails.PaymentDetailsBuilder paymentDetailsBuilder =
                PaymentDetails.builder()
                        .userId(SecurityUtils.currentUserId())
                        .paymentAmount(paymentAmount)
                        .siteName(SiteName.OUR_MALL)
                        .paymentStatus(PaymentStatus.PAYMENT_PROCESSING);

        if (paymentAmount == 0)
            paymentDetailsBuilder.paymentStatus(PaymentStatus.PAYMENT_COMPLETED);

        return paymentDetailsBuilder.build();
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
