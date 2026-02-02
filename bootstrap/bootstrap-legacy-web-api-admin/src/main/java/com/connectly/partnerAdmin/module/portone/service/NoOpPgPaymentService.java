package com.connectly.partnerAdmin.module.portone.service;

import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.connectly.partnerAdmin.module.seller.controller.request.CreateSellerSettlementAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * PortOne 비활성화 시 사용되는 NoOp 구현체
 * Stage 환경 등에서 외부 API 호출 없이 동작하도록 함
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "portone.enabled", havingValue = "false")
public class NoOpPgPaymentService implements PgPaymentService {

    @Override
    public void refundOrder(RefundOrder refundOrder) {
        log.warn("[NoOp] PortOne disabled - refundOrder skipped: {}", refundOrder);
    }

    @Override
    public String validateAccount(CreateSellerSettlementAccount createRefundAccount) {
        log.warn("[NoOp] PortOne disabled - validateAccount skipped");
        return "NoOp-Holder";
    }
}
