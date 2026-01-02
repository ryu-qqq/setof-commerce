package com.ryuqq.setof.application.shippingpolicy.service.query;

import com.ryuqq.setof.application.shippingpolicy.assembler.ShippingPolicyAssembler;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.application.shippingpolicy.manager.query.ShippingPolicyReadManager;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetDefaultShippingAddressUseCase;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import org.springframework.stereotype.Service;

/**
 * 기본 배송 정책 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ShippingPolicyReadManager로 배송 정책 조회
 *   <li>ShippingPolicyAssembler로 Response DTO 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DefaultShippingPolicyQueryService implements GetDefaultShippingAddressUseCase {

    private final ShippingPolicyReadManager shippingPolicyReadManager;
    private final ShippingPolicyAssembler shippingPolicyAssembler;

    public DefaultShippingPolicyQueryService(
            ShippingPolicyReadManager shippingPolicyReadManager,
            ShippingPolicyAssembler shippingPolicyAssembler) {
        this.shippingPolicyReadManager = shippingPolicyReadManager;
        this.shippingPolicyAssembler = shippingPolicyAssembler;
    }

    @Override
    public ShippingPolicyResponse execute(Long sellerId) {
        ShippingPolicy defaultBySellerId =
                shippingPolicyReadManager.findDefaultBySellerId(sellerId);
        return shippingPolicyAssembler.toResponse(defaultBySellerId);
    }
}
