package com.ryuqq.setof.application.seller.service.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.manager.command.SellerPersistenceManager;
import com.ryuqq.setof.application.seller.manager.query.SellerReadManager;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.ApprovalStatus;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 셀러 승인 상태 변경 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>SellerReadManager로 기존 셀러 조회
 *   <li>상태에 따른 도메인 메서드 호출 (approve, reject, suspend)
 *   <li>SellerPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateApprovalStatusService implements UpdateApprovalStatusUseCase {

    private final SellerReadManager sellerReadManager;
    private final SellerPersistenceManager sellerPersistenceManager;
    private final ClockHolder clockHolder;

    public UpdateApprovalStatusService(
            SellerReadManager sellerReadManager,
            SellerPersistenceManager sellerPersistenceManager,
            ClockHolder clockHolder) {
        this.sellerReadManager = sellerReadManager;
        this.sellerPersistenceManager = sellerPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UpdateApprovalStatusCommand command) {
        Seller existingSeller = sellerReadManager.findById(command.sellerId());
        ApprovalStatus targetStatus = ApprovalStatus.valueOf(command.approvalStatus());

        Instant now = Instant.now(clockHolder.getClock());
        Seller updatedSeller =
                switch (targetStatus) {
                    case APPROVED -> existingSeller.approve(now);
                    case REJECTED -> existingSeller.reject(now);
                    case SUSPENDED -> existingSeller.suspend(now);
                    case PENDING -> existingSeller; // PENDING 상태로의 변경은 허용하지 않음
                };

        if (updatedSeller != existingSeller) {
            sellerPersistenceManager.persist(updatedSeller);
        }
    }
}
