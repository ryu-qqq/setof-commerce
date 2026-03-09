package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.client.PaymentGatewayClient;
import org.springframework.stereotype.Component;

/**
 * PaymentGatewayManager - 외부 PG 클라이언트 래핑 Manager.
 *
 * <p>외부 PG사 API 호출을 담당합니다. 트랜잭션 없음.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentGatewayManager {

    private final PaymentGatewayClient paymentGatewayClient;

    public PaymentGatewayManager(PaymentGatewayClient paymentGatewayClient) {
        this.paymentGatewayClient = paymentGatewayClient;
    }

    /**
     * PG사 거래 ID로 결제 완료 여부 확인.
     *
     * @param pgAgencyId PG사 거래 ID
     * @return 결제 완료 여부. PG 조회 실패 시 false.
     */
    public boolean isPaid(String pgAgencyId) {
        return paymentGatewayClient.isPaid(pgAgencyId).orElse(false);
    }
}
