package com.ryuqq.setof.adapter.out.client.authhub;

import com.ryuqq.setof.application.seller.dto.response.SellerIdentityProvisioningResult;
import com.ryuqq.setof.application.seller.port.out.client.IdentityClient;
import com.ryuqq.setof.domain.seller.aggregate.SellerAuthOutbox;
import org.springframework.stereotype.Component;

/**
 * AuthHub Identity 서비스 클라이언트 어댑터.
 *
 * <p>AuthHub API와 통신하여 Tenant/Organization을 생성합니다.
 */
@Component
public class AuthHubIdentityClientAdapter implements IdentityClient {

    @Override
    public SellerIdentityProvisioningResult provisionSellerIdentity(SellerAuthOutbox outbox) {
        // TODO: AuthHub SDK 연동 구현 필요
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
