package com.ryuqq.setof.application.selleradmin.internal;

import com.ryuqq.setof.application.selleradmin.manager.SellerAdminAuthOutboxCommandManager;
import com.ryuqq.setof.application.selleradmin.manager.SellerAdminCommandManager;
import com.ryuqq.setof.domain.selleradmin.aggregate.SellerAdmin;
import com.ryuqq.setof.domain.selleradmin.aggregate.SellerAdminAuthOutbox;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 셀러 관리자 인증 완료 Facade.
 *
 * <p>Outbox 완료와 SellerAdmin 인증 정보 업데이트를 하나의 트랜잭션으로 묶어 원자성을 보장합니다.
 *
 * <p>두 작업이 별도 트랜잭션으로 수행되면 데이터 불일치가 발생할 수 있습니다:
 *
 * <ul>
 *   <li>Outbox는 COMPLETED인데 SellerAdmin에 authUserId가 없는 상태
 *   <li>이 경우 재처리도 불가능하고 수동 복구가 필요
 * </ul>
 */
@Component
public class SellerAdminAuthCompletionFacade {

    private final SellerAdminAuthOutboxCommandManager outboxCommandManager;
    private final SellerAdminCommandManager sellerAdminCommandManager;

    public SellerAdminAuthCompletionFacade(
            SellerAdminAuthOutboxCommandManager outboxCommandManager,
            SellerAdminCommandManager sellerAdminCommandManager) {
        this.outboxCommandManager = outboxCommandManager;
        this.sellerAdminCommandManager = sellerAdminCommandManager;
    }

    /**
     * 인증 완료 처리를 원자적으로 수행합니다.
     *
     * <p>Outbox 상태를 COMPLETED로 변경하고, SellerAdmin에 인증 정보를 저장합니다.
     *
     * @param outbox 처리할 Outbox
     * @param sellerAdmin 업데이트할 셀러 관리자
     * @param authUserId 인증 서버 사용자 ID
     * @param now 완료 시각
     */
    @Transactional
    public void completeAuthOutbox(
            SellerAdminAuthOutbox outbox, SellerAdmin sellerAdmin, String authUserId, Instant now) {
        outbox.complete(now);
        outboxCommandManager.persist(outbox);

        sellerAdmin.updateAuthUserId(authUserId, now);
        sellerAdminCommandManager.persist(sellerAdmin);
    }
}
