package com.ryuqq.setof.adapter.in.rest.v2.claim.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.command.RequestClaimV2ApiRequest;
import com.ryuqq.setof.application.claim.dto.command.RequestClaimCommand;
import org.springframework.stereotype.Component;

/**
 * ClaimV2ApiMapper - Claim API DTO 변환 Mapper
 *
 * <p>API Request DTO → Application Command 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimV2ApiMapper {

    /**
     * RequestClaimV2ApiRequest → RequestClaimCommand 변환
     *
     * @param request API 요청 DTO
     * @return Application Command
     */
    public RequestClaimCommand toCommand(RequestClaimV2ApiRequest request) {
        return new RequestClaimCommand(
                request.orderId(),
                request.orderItemId(),
                request.claimType(),
                request.claimReason(),
                request.claimReasonDetail(),
                request.quantity(),
                request.refundAmount());
    }
}
