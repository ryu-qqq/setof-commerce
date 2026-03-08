package com.ryuqq.setof.application.refundaccount.manager;

import com.ryuqq.setof.application.refundaccount.port.out.command.RefundAccountCommandPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefundAccountCommandManager - 환불 계좌 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountCommandManager {

    private final RefundAccountCommandPort commandPort;

    public RefundAccountCommandManager(RefundAccountCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @Transactional
    public Long persist(RefundAccount refundAccount) {
        return commandPort.persist(refundAccount);
    }
}
