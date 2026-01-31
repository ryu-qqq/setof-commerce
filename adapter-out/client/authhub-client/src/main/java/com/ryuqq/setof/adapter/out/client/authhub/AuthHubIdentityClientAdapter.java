package com.ryuqq.setof.adapter.out.client.authhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.authhub.sdk.api.OnboardingApi;
import com.ryuqq.authhub.sdk.exception.AuthHubBadRequestException;
import com.ryuqq.authhub.sdk.exception.AuthHubException;
import com.ryuqq.authhub.sdk.exception.AuthHubServerException;
import com.ryuqq.authhub.sdk.model.common.ApiResponse;
import com.ryuqq.authhub.sdk.model.onboarding.TenantOnboardingRequest;
import com.ryuqq.authhub.sdk.model.onboarding.TenantOnboardingResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerIdentityProvisioningResult;
import com.ryuqq.setof.application.seller.port.out.client.IdentityClient;
import com.ryuqq.setof.domain.seller.aggregate.SellerAuthOutbox;
import org.springframework.stereotype.Component;

/**
 * AuthHub Identity 서비스 클라이언트 어댑터.
 *
 * <p>AuthHub SDK를 사용하여 Tenant/Organization을 생성합니다.
 *
 * <p>Outbox 패턴과 함께 사용되며, 멱등키를 통해 중복 요청을 방지합니다.
 */
@Component
public class AuthHubIdentityClientAdapter implements IdentityClient {

    private final OnboardingApi onboardingApi;
    private final ObjectMapper objectMapper;

    public AuthHubIdentityClientAdapter(OnboardingApi onboardingApi, ObjectMapper objectMapper) {
        this.onboardingApi = onboardingApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public SellerIdentityProvisioningResult provisionSellerIdentity(SellerAuthOutbox outbox) {
        TenantOnboardingRequest request = parsePayload(outbox.payload());
        String idempotencyKey = outbox.idempotencyKeyValue();

        try {
            ApiResponse<TenantOnboardingResponse> response =
                    onboardingApi.onboard(request, idempotencyKey);

            TenantOnboardingResponse data = response.data();
            return SellerIdentityProvisioningResult.success(data.tenantId(), data.organizationId());

        } catch (AuthHubBadRequestException e) {
            return SellerIdentityProvisioningResult.permanentFailure("BAD_REQUEST", e.getMessage());

        } catch (AuthHubServerException e) {
            return SellerIdentityProvisioningResult.retryableFailure(
                    "SERVER_ERROR", e.getMessage());

        } catch (AuthHubException e) {
            boolean retryable = isRetryableError(e);
            if (retryable) {
                return SellerIdentityProvisioningResult.retryableFailure(
                        "AUTHHUB_ERROR", e.getMessage());
            }
            return SellerIdentityProvisioningResult.permanentFailure(
                    "AUTHHUB_ERROR", e.getMessage());
        }
    }

    private TenantOnboardingRequest parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, TenantOnboardingRequest.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    "Failed to parse outbox payload: " + e.getMessage(), e);
        }
    }

    private boolean isRetryableError(AuthHubException e) {
        return e instanceof AuthHubServerException;
    }
}
