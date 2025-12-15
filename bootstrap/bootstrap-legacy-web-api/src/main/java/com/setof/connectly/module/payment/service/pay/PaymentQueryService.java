package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.payment.dto.payment.CreatePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePaymentInCart;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.enums.PaymentMethodGroup;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {

    PaymentMethodGroup getPaymentMethodGroup();

    PaymentGatewayRequestDto doPay(CreatePayment payment);

    PaymentGatewayRequestDto doPayInCart(CreatePaymentInCart payment);

    Optional<PaymentBill> doPayWebHook(PgProviderTransDto pgProviderTransDto);

    void doPayFailed(long paymentId, List<Long> cartIds);

    void doPayRefund(PgProviderTransDto pgProviderTransDto, RefundOrderSheet refundOrderSheet);
}
