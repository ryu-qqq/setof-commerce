package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ApproveClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.CompleteClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmExchangeDeliveredV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmReturnReceivedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterExchangeShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterReturnShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RejectClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ScheduleReturnPickupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.UpdateReturnShippingStatusV2ApiRequest;
import com.ryuqq.setof.application.claim.dto.command.ApproveClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.CompleteClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.ConfirmExchangeDeliveredCommand;
import com.ryuqq.setof.application.claim.dto.command.ConfirmReturnReceivedCommand;
import com.ryuqq.setof.application.claim.dto.command.RegisterExchangeShippingCommand;
import com.ryuqq.setof.application.claim.dto.command.RegisterReturnShippingCommand;
import com.ryuqq.setof.application.claim.dto.command.RejectClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.ScheduleReturnPickupCommand;
import com.ryuqq.setof.application.claim.dto.command.UpdateReturnShippingStatusCommand;
import org.springframework.stereotype.Component;

/**
 * ClaimAdminV2ApiMapper - Claim Admin API DTO 변환 Mapper
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimAdminV2ApiMapper {

    /** ApproveClaimV2ApiRequest → ApproveClaimCommand 변환 */
    public ApproveClaimCommand toApproveCommand(String claimId, ApproveClaimV2ApiRequest request) {
        return new ApproveClaimCommand(claimId, request.adminId());
    }

    /** RejectClaimV2ApiRequest → RejectClaimCommand 변환 */
    public RejectClaimCommand toRejectCommand(String claimId, RejectClaimV2ApiRequest request) {
        return new RejectClaimCommand(claimId, request.adminId(), request.rejectReason());
    }

    /** CompleteClaimV2ApiRequest → CompleteClaimCommand 변환 */
    public CompleteClaimCommand toCompleteCommand(
            String claimId, CompleteClaimV2ApiRequest request) {
        return new CompleteClaimCommand(claimId, request.adminId());
    }

    // ========== 반품 배송 관련 매핑 ==========

    /** ScheduleReturnPickupV2ApiRequest → ScheduleReturnPickupCommand 변환 */
    public ScheduleReturnPickupCommand toScheduleReturnPickupCommand(
            String claimId, ScheduleReturnPickupV2ApiRequest request) {
        return new ScheduleReturnPickupCommand(
                claimId, request.scheduledAt(), request.pickupAddress(), request.customerPhone());
    }

    /**
     * RegisterReturnShippingV2ApiRequest → RegisterReturnShippingCommand 변환
     *
     * <p>Domain VO 의존성 제거를 위해 String 기반 팩토리 메서드 사용
     */
    public RegisterReturnShippingCommand toRegisterReturnShippingCommand(
            String claimId, RegisterReturnShippingV2ApiRequest request) {
        return RegisterReturnShippingCommand.ofStrings(
                claimId, request.shippingMethod(), request.trackingNumber(), request.carrier());
    }

    /**
     * UpdateReturnShippingStatusV2ApiRequest → UpdateReturnShippingStatusCommand 변환
     *
     * <p>Domain VO 의존성 제거를 위해 String 기반 팩토리 메서드 사용
     */
    public UpdateReturnShippingStatusCommand toUpdateReturnShippingStatusCommand(
            String claimId, UpdateReturnShippingStatusV2ApiRequest request) {
        return UpdateReturnShippingStatusCommand.ofStrings(
                claimId, request.status(), request.trackingNumber());
    }

    /**
     * ConfirmReturnReceivedV2ApiRequest → ConfirmReturnReceivedCommand 변환
     *
     * <p>Domain VO 의존성 제거를 위해 String 기반 팩토리 메서드 사용
     */
    public ConfirmReturnReceivedCommand toConfirmReturnReceivedCommand(
            String claimId, ConfirmReturnReceivedV2ApiRequest request) {
        return ConfirmReturnReceivedCommand.ofStrings(
                claimId, request.inspectionResult(), request.inspectionNote());
    }

    // ========== 교환 배송 관련 매핑 ==========

    /** RegisterExchangeShippingV2ApiRequest → RegisterExchangeShippingCommand 변환 */
    public RegisterExchangeShippingCommand toRegisterExchangeShippingCommand(
            String claimId, RegisterExchangeShippingV2ApiRequest request) {
        return new RegisterExchangeShippingCommand(
                claimId, request.trackingNumber(), request.carrier());
    }

    /** ConfirmExchangeDeliveredV2ApiRequest → ConfirmExchangeDeliveredCommand 변환 */
    public ConfirmExchangeDeliveredCommand toConfirmExchangeDeliveredCommand(
            String claimId, ConfirmExchangeDeliveredV2ApiRequest request) {
        return new ConfirmExchangeDeliveredCommand(claimId);
    }
}
