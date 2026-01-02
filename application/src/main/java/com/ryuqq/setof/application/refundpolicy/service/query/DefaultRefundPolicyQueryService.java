package com.ryuqq.setof.application.refundpolicy.service.query;

import com.ryuqq.setof.application.refundpolicy.assembler.RefundPolicyAssembler;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.manager.query.RefundPolicyReadManager;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetDefaultRefundPolicyUseCase;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import org.springframework.stereotype.Service;

/**
 * 기본 환불 정책 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>RefundPolicyReadManager로 환불 정책 조회
 *   <li>RefundPolicyAssembler로 Response DTO 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DefaultRefundPolicyQueryService implements GetDefaultRefundPolicyUseCase {

    private final RefundPolicyReadManager refundPolicyReadManager;
    private final RefundPolicyAssembler refundPolicyAssembler;

    public DefaultRefundPolicyQueryService(
            RefundPolicyReadManager refundPolicyReadManager,
            RefundPolicyAssembler refundPolicyAssembler) {
        this.refundPolicyReadManager = refundPolicyReadManager;
        this.refundPolicyAssembler = refundPolicyAssembler;
    }

    @Override
    public RefundPolicyResponse execute(Long sellerId) {
        RefundPolicy defaultBySellerId = refundPolicyReadManager.findDefaultBySellerId(sellerId);
        return refundPolicyAssembler.toResponse(defaultBySellerId);
    }
}
