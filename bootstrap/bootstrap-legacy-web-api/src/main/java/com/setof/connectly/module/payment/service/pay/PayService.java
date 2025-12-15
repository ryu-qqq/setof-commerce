package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.payment.dto.payment.FailPayment;
import com.setof.connectly.module.payment.dto.payment.FailPaymentResponse;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.dto.refund.RefundPaymentDto;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import com.setof.connectly.module.portone.dto.PortOneWebHookDto;

public interface PayService {

    <T extends BasePayment> PaymentGatewayRequestDto pay(T t);

    FailPaymentResponse payFailed(FailPayment failPayment);

    RefundPaymentDto refundOrder(long paymentId, RefundOrderSheet refundOrderSheet);

    PortOneWebHookDto paymentWebHook(PortOneWebHookDto portOneWebHookDto);

    void payCompleted(PgProviderTransDto pgProviderTransDto);
}
