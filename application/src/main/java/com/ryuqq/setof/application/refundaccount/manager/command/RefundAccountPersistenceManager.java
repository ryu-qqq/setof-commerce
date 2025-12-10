package com.ryuqq.setof.application.refundaccount.manager.command;

import com.ryuqq.setof.application.refundaccount.port.out.command.RefundAccountPersistencePort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefundAccount Persistence Manager
 *
 * <p>RefundAccount 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountPersistenceManager {

    private final RefundAccountPersistencePort refundAccountPersistencePort;

    public RefundAccountPersistenceManager(RefundAccountPersistencePort refundAccountPersistencePort) {
        this.refundAccountPersistencePort = refundAccountPersistencePort;
    }

    /**
     * RefundAccount 저장
     *
     * <p>JPA dirty checking으로 수정 시에도 persist 호출로 처리
     *
     * @param refundAccount 저장할 RefundAccount
     * @return 저장된 RefundAccount의 ID
     */
    @Transactional
    public RefundAccountId persist(RefundAccount refundAccount) {
        return refundAccountPersistencePort.persist(refundAccount);
    }
}
