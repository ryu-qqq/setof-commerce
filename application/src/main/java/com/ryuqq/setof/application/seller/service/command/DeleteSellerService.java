package com.ryuqq.setof.application.seller.service.command;

import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;
import com.ryuqq.setof.application.seller.manager.command.SellerPersistenceManager;
import com.ryuqq.setof.application.seller.manager.query.SellerReadManager;
import com.ryuqq.setof.application.seller.port.in.command.DeleteSellerUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 셀러 삭제 서비스 (Soft Delete)
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>SellerReadManager로 기존 셀러 조회
 *   <li>도메인 delete() 메서드 호출
 *   <li>SellerPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteSellerService implements DeleteSellerUseCase {

    private final SellerReadManager sellerReadManager;
    private final SellerPersistenceManager sellerPersistenceManager;
    private final ClockHolder clockHolder;

    public DeleteSellerService(
            SellerReadManager sellerReadManager,
            SellerPersistenceManager sellerPersistenceManager,
            ClockHolder clockHolder) {
        this.sellerReadManager = sellerReadManager;
        this.sellerPersistenceManager = sellerPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(DeleteSellerCommand command) {
        Seller existingSeller = sellerReadManager.findById(command.sellerId());

        if (existingSeller.isDeleted()) {
            return;
        }

        Instant now = Instant.now(clockHolder.getClock());
        Seller deletedSeller = existingSeller.delete(now);

        sellerPersistenceManager.persist(deletedSeller);
    }
}
