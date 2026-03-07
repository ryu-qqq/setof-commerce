package com.ryuqq.setof.application.refundaccount.port.in.query;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;

/**
 * 환불 계좌 단건 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetRefundAccountUseCase {

    RefundAccountResult execute(Long userId);
}
