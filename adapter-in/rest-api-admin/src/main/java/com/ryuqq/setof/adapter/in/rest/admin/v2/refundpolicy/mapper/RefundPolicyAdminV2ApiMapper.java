package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyV2ApiRequest;
import com.ryuqq.setof.application.refundpolicy.dto.command.DeleteRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.SetDefaultRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchQuery;
import org.springframework.stereotype.Component;

/**
 * RefundPolicy Admin V2 API Mapper
 *
 * <p>환불 정책 관리 API DTO ↔ Application Command 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class RefundPolicyAdminV2ApiMapper {

    /** 환불 정책 등록 요청 → 등록 커맨드 변환 */
    public RegisterRefundPolicyCommand toRegisterCommand(RegisterRefundPolicyV2ApiRequest request) {
        return new RegisterRefundPolicyCommand(
                request.sellerId(),
                request.policyName(),
                request.returnAddressLine1(),
                request.returnAddressLine2(),
                request.returnZipCode(),
                request.refundPeriodDays(),
                request.refundDeliveryCost(),
                request.refundGuide(),
                request.isDefault(),
                request.displayOrder());
    }

    /** 환불 정책 수정 요청 → 수정 커맨드 변환 */
    public UpdateRefundPolicyCommand toUpdateCommand(
            Long refundPolicyId, UpdateRefundPolicyV2ApiRequest request) {
        return new UpdateRefundPolicyCommand(
                refundPolicyId,
                request.policyName(),
                request.returnAddressLine1(),
                request.returnAddressLine2(),
                request.returnZipCode(),
                request.refundPeriodDays(),
                request.refundDeliveryCost(),
                request.refundGuide());
    }

    /** 기본 정책 설정 커맨드 생성 */
    public SetDefaultRefundPolicyCommand toSetDefaultCommand(Long refundPolicyId, Long sellerId) {
        return new SetDefaultRefundPolicyCommand(refundPolicyId, sellerId);
    }

    /** 환불 정책 삭제 커맨드 생성 */
    public DeleteRefundPolicyCommand toDeleteCommand(Long refundPolicyId, Long sellerId) {
        return new DeleteRefundPolicyCommand(refundPolicyId, sellerId);
    }

    /** 검색 쿼리 생성 */
    public RefundPolicySearchQuery toSearchQuery(Long sellerId, boolean includeDeleted) {
        return new RefundPolicySearchQuery(sellerId, includeDeleted);
    }
}
