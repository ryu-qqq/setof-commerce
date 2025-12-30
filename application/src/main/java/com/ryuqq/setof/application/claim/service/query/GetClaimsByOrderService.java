package com.ryuqq.setof.application.claim.service.query;

import com.ryuqq.setof.application.claim.assembler.ClaimAssembler;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.claim.port.in.query.GetClaimsByOrderUseCase;
import com.ryuqq.setof.application.claim.port.out.query.ClaimQueryPort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.order.vo.OrderId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetClaimsByOrderService - 주문별 클레임 목록 조회 Service
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
@Transactional(readOnly = true)
public class GetClaimsByOrderService implements GetClaimsByOrderUseCase {

    private final ClaimQueryPort claimQueryPort;
    private final ClaimAssembler claimAssembler;

    public GetClaimsByOrderService(ClaimQueryPort claimQueryPort, ClaimAssembler claimAssembler) {
        this.claimQueryPort = claimQueryPort;
        this.claimAssembler = claimAssembler;
    }

    @Override
    public List<ClaimResponse> getByOrderId(String orderId) {
        List<Claim> claims = claimQueryPort.findByOrderId(OrderId.fromString(orderId));
        return claims.stream().map(claimAssembler::toResponse).toList();
    }
}
