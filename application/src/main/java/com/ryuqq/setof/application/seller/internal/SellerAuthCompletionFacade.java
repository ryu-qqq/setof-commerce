package com.ryuqq.setof.application.seller.internal;

import com.ryuqq.setof.application.seller.manager.SellerAuthOutboxCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCommandManager;
import com.ryuqq.setof.domain.seller.aggregate.SellerAuthOutbox;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 셀러 인증 완료 Facade.
 *
 * <p>Outbox 완료와 Seller 인증 정보 업데이트를 하나의 트랜잭션으로 묶어 원자성을 보장합니다.
 *
 * <p>두 작업이 별도 트랜잭션으로 수행되면 데이터 불일치가 발생할 수 있습니다:
 *
 * <ul>
 *   <li>Outbox는 COMPLETED인데 Seller에 tenantId/organizationId가 없는 상태
 *   <li>이 경우 재처리도 불가능하고 수동 복구가 필요
 * </ul>
 */
@Component
public class SellerAuthCompletionFacade {

    private final SellerAuthOutboxCommandManager outboxCommandManager;
    private final SellerCommandManager sellerCommandManager;

    public SellerAuthCompletionFacade(
            SellerAuthOutboxCommandManager outboxCommandManager,
            SellerCommandManager sellerCommandManager) {
        this.outboxCommandManager = outboxCommandManager;
        this.sellerCommandManager = sellerCommandManager;
    }

    /**
     * 인증 완료 처리를 원자적으로 수행합니다.
     *
     * <p>Outbox 상태를 COMPLETED로 변경하고, Seller에 인증 정보를 저장합니다.
     *
     * @param outbox 처리할 Outbox
     * @param tenantId 인증 서버 테넌트 ID
     * @param organizationId 인증 서버 조직 ID
     * @param now 완료 시각
     */
    @Transactional
    public void completeAuthOutbox(
            SellerAuthOutbox outbox, String tenantId, String organizationId, Instant now) {
        outbox.complete(now);
        outboxCommandManager.persist(outbox);
        sellerCommandManager.updateAuthInfo(outbox.sellerId(), tenantId, organizationId);
    }
}
