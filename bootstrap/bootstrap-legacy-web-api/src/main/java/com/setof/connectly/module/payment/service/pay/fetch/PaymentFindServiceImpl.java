package com.setof.connectly.module.payment.service.pay.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.exception.payment.PaymentNotFoundException;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.dto.OrderRejectReason;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.payment.dto.filter.PaymentFilter;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import com.setof.connectly.module.payment.entity.Payment;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import com.setof.connectly.module.payment.mapper.PaymentMapper;
import com.setof.connectly.module.payment.mapper.PaymentSliceMapper;
import com.setof.connectly.module.payment.repository.PaymentFindRepository;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentFindServiceImpl implements PaymentFindService {
    private final PaymentFindRepository paymentFindRepository;
    private final OrderFindService orderFindService;
    private final PaymentMapper paymentMapper;
    private final PaymentSliceMapper paymentSliceMapper;

    @Override
    public PaymentStatus fetchPaymentStatus(long paymentId) {
        return paymentFindRepository
                .fetchPaymentStatus(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Override
    public Payment fetchPaymentEntity(long paymentId) {
        long userId = SecurityUtils.currentUserId();
        return paymentFindRepository
                .fetchPaymentEntity(paymentId, userId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Override
    public Payment fetchPaymentEntityForWebhook(long paymentId) {
        return paymentFindRepository
                .fetchPaymentEntity(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Override
    public PaymentResponse fetchPaymentForSlack(long paymentId) {
        PaymentResponse paymentResponse =
                paymentFindRepository
                        .fetchPayment(paymentId)
                        .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        ArrayList<Long> orderIds = new ArrayList<>(paymentResponse.getPayment().getOrderIds());
        Set<OrderProductDto> options =
                orderFindService.fetchOrderProducts(orderIds, new ArrayList<>());
        return paymentMapper.toPaymentResponse(paymentResponse, options);
    }

    @Override
    public PaymentResponse fetchPayment(long paymentId) {
        long userId = SecurityUtils.currentUserId();

        PaymentResponse paymentResponse =
                paymentFindRepository
                        .fetchPayment(paymentId, userId)
                        .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        ArrayList<Long> orderIds = new ArrayList<>(paymentResponse.getPayment().getOrderIds());
        Set<OrderProductDto> options =
                orderFindService.fetchOrderProducts(orderIds, new ArrayList<>());
        List<OrderRejectReason> orderRejectReasons = orderFindService.fetchRejectedOrder(orderIds);
        return paymentMapper.toPaymentResponse(paymentResponse, options, orderRejectReasons);
    }

    @Override
    public CustomSlice<PaymentResponse> fetchPayments(PaymentFilter filter, Pageable pageable) {
        long userId = SecurityUtils.currentUserId();
        List<PaymentResponse> findPayments =
                paymentFindRepository.fetchPayments(filter, pageable, userId);

        List<Long> orderIds =
                findPayments.stream()
                        .flatMap(
                                paymentResponse ->
                                        paymentResponse.getPayment().getOrderIds().stream())
                        .collect(Collectors.toList());

        Set<OrderProductDto> options =
                orderFindService.fetchOrderProducts(orderIds, filter.getOrderStatusList());

        List<Long> targetOrderIds =
                options.stream().map(OrderProductDto::getOrderId).collect(Collectors.toList());

        List<OrderRejectReason> orderRejectReasons =
                orderFindService.fetchRejectedOrder(targetOrderIds);

        List<PaymentResponse> paymentResponses =
                paymentMapper.toPaymentResponses(findPayments, options, orderRejectReasons);
        return paymentSliceMapper.toSlice(paymentResponses, pageable);
    }
}
