package com.ryuqq.setof.application.refundpolicy.service.query;

import com.ryuqq.setof.application.refundpolicy.assembler.RefundPolicyAssembler;
import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchQuery;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.manager.query.RefundPolicyReadManager;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPoliciesUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPolicyUseCase;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 환불 정책 조회 서비스
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
public class RefundPolicyQueryService implements GetRefundPolicyUseCase, GetRefundPoliciesUseCase {

    private final RefundPolicyReadManager refundPolicyReadManager;
    private final RefundPolicyAssembler refundPolicyAssembler;

    public RefundPolicyQueryService(
            RefundPolicyReadManager refundPolicyReadManager,
            RefundPolicyAssembler refundPolicyAssembler) {
        this.refundPolicyReadManager = refundPolicyReadManager;
        this.refundPolicyAssembler = refundPolicyAssembler;
    }

    @Override
    public RefundPolicyResponse execute(Long refundPolicyId) {
        RefundPolicy refundPolicy = refundPolicyReadManager.findById(refundPolicyId);
        return refundPolicyAssembler.toResponse(refundPolicy);
    }

    @Override
    public List<RefundPolicyResponse> execute(RefundPolicySearchQuery query) {
        List<RefundPolicy> refundPolicies =
                refundPolicyReadManager.findBySellerId(query.sellerId(), query.includeDeleted());
        return refundPolicyAssembler.toResponses(refundPolicies);
    }
}
