package com.ryuqq.setof.application.refundaccount.manager;

import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import com.ryuqq.setof.application.refundaccount.port.out.query.RefundAccountQueryPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefundAccountReadManager - 환불 계좌 조회 Manager.
 *
 * <p>QueryPort에서 도메인 객체를 받고, Assembler를 통해 Result DTO로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountReadManager {

    private final RefundAccountQueryPort queryPort;
    private final RefundAccountAssembler assembler;

    public RefundAccountReadManager(
            RefundAccountQueryPort queryPort, RefundAccountAssembler assembler) {
        this.queryPort = queryPort;
        this.assembler = assembler;
    }

    @Transactional(readOnly = true)
    public RefundAccountResult fetchRefundAccount(long userId) {
        RefundAccount refundAccount =
                queryPort
                        .fetchRefundAccount(userId)
                        .orElseThrow(RefundAccountNotFoundException::new);
        return assembler.toResult(refundAccount);
    }

    @Transactional(readOnly = true)
    public RefundAccount getByUserIdAndId(long userId, long refundAccountId) {
        return queryPort
                .findByUserIdAndId(userId, refundAccountId)
                .orElseThrow(() -> new RefundAccountNotFoundException(refundAccountId));
    }
}
