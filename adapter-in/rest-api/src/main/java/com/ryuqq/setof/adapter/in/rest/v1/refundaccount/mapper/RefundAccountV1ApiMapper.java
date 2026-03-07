package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.RegisterRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.UpdateRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import org.springframework.stereotype.Component;

/**
 * RefundAccountV1ApiMapper - 환불 계좌 V1 Public API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-004: API Request → Application Command 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountV1ApiMapper {

    public RefundAccountV1ApiResponse toResponse(RefundAccountResult result) {
        return new RefundAccountV1ApiResponse(
                result.refundAccountId(),
                result.bankName(),
                result.accountNumber(),
                result.accountHolderName());
    }

    public RegisterRefundAccountCommand toRegisterCommand(
            Long userId, RegisterRefundAccountV1ApiRequest request) {
        return new RegisterRefundAccountCommand(
                userId, request.bankName(), request.accountNumber(), request.accountHolderName());
    }

    public UpdateRefundAccountCommand toUpdateCommand(
            Long userId, Long refundAccountId, UpdateRefundAccountV1ApiRequest request) {
        return new UpdateRefundAccountCommand(
                userId,
                refundAccountId,
                request.bankName(),
                request.accountNumber(),
                request.accountHolderName());
    }

    public DeleteRefundAccountCommand toDeleteCommand(Long userId, Long refundAccountId) {
        return new DeleteRefundAccountCommand(userId, refundAccountId);
    }
}
