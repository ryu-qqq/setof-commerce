package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.payment.dto.payment.FailPayment;
import com.setof.connectly.module.payment.dto.payment.FailPaymentResponse;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.dto.refund.RefundPaymentDto;
import com.setof.connectly.module.payment.mapper.PaymentMapper;
import com.setof.connectly.module.payment.service.PaymentQueryStrategy;
import com.setof.connectly.module.payment.service.bill.fetch.PaymentBillFindService;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import com.setof.connectly.module.portone.dto.PortOneTransDto;
import com.setof.connectly.module.portone.dto.PortOneWebHookDto;
import com.setof.connectly.module.portone.mapper.PortOneMapper;
import com.setof.connectly.module.portone.service.payment.PgPaymentService;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class BasePayService implements PayService {

    private final PaymentQueryStrategy paymentQueryStrategy;
    private final PgPaymentService pgPaymentService;
    private final PortOneMapper portOneMapper;
    private final PaymentMapper paymentMapper;
    private final PaymentBillFindService paymentBillFindService;

    @Override
    public <T extends BasePayment> PaymentGatewayRequestDto pay(T basePayment) {
        PaymentQueryService serviceByPayMethod =
                paymentQueryStrategy.getServiceByPayMethod(basePayment.getPayMethod());
        return basePayment.processPayment(serviceByPayMethod);
    }

    @Override
    public FailPaymentResponse payFailed(FailPayment failPayment) {
        FailPaymentResponse failPaymentResponse =
                paymentBillFindService.fetchPaymentMethod(failPayment.getPaymentUniqueId());
        PaymentQueryService serviceByPayMethod =
                paymentQueryStrategy.getServiceByPayMethod(failPaymentResponse.getPaymentMethod());
        serviceByPayMethod.doPayFailed(
                failPaymentResponse.getPaymentId(), failPayment.getCartIds());
        return failPaymentResponse;
    }

    @Override
    public RefundPaymentDto refundOrder(long paymentId, RefundOrderSheet refundOrderSheet) {
        String paymentAgencyId = paymentBillFindService.fetchPaymentAgencyId(paymentId);
        Payment payment = pgPaymentService.getPayment(paymentAgencyId);
        PortOneTransDto portOneTransDto = portOneMapper.convertPayment(payment);
        PaymentQueryService serviceByPayMethod =
                paymentQueryStrategy.getServiceByPayMethod(portOneTransDto.getPayMethod());

        serviceByPayMethod.doPayRefund(portOneTransDto, refundOrderSheet);

        if (refundOrderSheet.getOrderStatus().isCancelOrder()) {
            pgPaymentService.refundOrder(payment.getImpUid(), paymentId, refundOrderSheet);
        }

        return paymentMapper.toRefundResult(portOneTransDto.getPaymentId(), refundOrderSheet);
    }

    @Override
    public PortOneWebHookDto paymentWebHook(PortOneWebHookDto portOneWebHookDto) {
        Payment payment = pgPaymentService.getPayment(portOneWebHookDto.getImpUid());
        PortOneTransDto portOneTransDto = portOneMapper.convertPayment(payment);
        payCompleted(portOneTransDto);
        return portOneWebHookDto;
    }

    @Override
    public void payCompleted(PgProviderTransDto pgProviderTransDto) {
        PaymentQueryService serviceByPayMethod =
                paymentQueryStrategy.getServiceByPayMethod(pgProviderTransDto.getPayMethod());
        serviceByPayMethod.doPayWebHook(pgProviderTransDto);
    }
}
