package com.ryuqq.setof.adapter.out.client.portone.adapter;

import com.ryuqq.setof.adapter.out.client.portone.client.PortOneClient;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOnePaymentResponse;
import com.ryuqq.setof.application.payment.port.out.client.PaymentGatewayClient;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * PaymentGatewayAdapter - PortOne V2 결제 상태 조회 어댑터.
 *
 * <p>PaymentGatewayClient Port를 구현하여 PortOne API로 결제 완료 여부를 확인합니다.
 *
 * <p>PortOne 비활성화 시 empty를 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentGatewayAdapter implements PaymentGatewayClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewayAdapter.class);

    private final PortOneClient portOneClient;
    private final PortOneProperties properties;

    public PaymentGatewayAdapter(PortOneClient portOneClient, PortOneProperties properties) {
        this.portOneClient = portOneClient;
        this.properties = properties;
    }

    @Override
    public Optional<Boolean> isPaid(String pgAgencyId) {
        if (!properties.isEnabled()) {
            log.info(
                    "PortOne is disabled. Skipping payment status check. pgAgencyId={}",
                    pgAgencyId);
            return Optional.empty();
        }

        try {
            PortOnePaymentResponse response = portOneClient.fetchPayment(pgAgencyId);

            if (response == null) {
                log.warn("Payment not found from PortOne. pgAgencyId={}", pgAgencyId);
                return Optional.empty();
            }

            return Optional.of(response.isPaid());

        } catch (Exception e) {
            log.error("Payment status check failed. pgAgencyId={}", pgAgencyId, e);
            return Optional.empty();
        }
    }
}
