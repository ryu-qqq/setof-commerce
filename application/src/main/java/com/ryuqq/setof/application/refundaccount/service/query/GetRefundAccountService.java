package com.ryuqq.setof.application.refundaccount.service.query;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.in.query.GetRefundAccountUseCase;
import org.springframework.stereotype.Service;

/**
 * GetRefundAccountService - 환불 계좌 단건 조회 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetRefundAccountService implements GetRefundAccountUseCase {

    private final RefundAccountReadManager readManager;

    public GetRefundAccountService(RefundAccountReadManager readManager) {
        this.readManager = readManager;
    }

    @Override
    public RefundAccountResult execute(Long userId) {
        return readManager.fetchRefundAccount(userId);
    }
}
