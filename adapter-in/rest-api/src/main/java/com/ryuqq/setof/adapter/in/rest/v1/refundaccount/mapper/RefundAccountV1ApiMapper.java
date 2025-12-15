package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.command.RefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * RefundAccount V1 API Mapper
 *
 * <p>환불계좌 관련 V1 API DTO ↔ Application Command/Response 변환
 *
 * <p>레거시 V1 스펙 유지하면서 새로운 UseCase 호출을 위한 변환
 *
 * <p>V1은 bankName으로 은행을 식별하며, bankName 기반 Command를 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Component
@Deprecated
public class RefundAccountV1ApiMapper {

    /**
     * V1 환불계좌 등록 요청 → 등록 커맨드 변환
     *
     * <p>V1에서는 bankName을 사용하여 은행을 식별합니다.
     *
     * @param memberId 회원 ID
     * @param request V1 API 요청
     * @return RegisterRefundAccountByBankNameCommand
     */
    public RegisterRefundAccountByBankNameCommand toRegisterCommand(
            UUID memberId, RefundAccountV1ApiRequest request) {
        return RegisterRefundAccountByBankNameCommand.of(
                memberId, request.bankName(), request.accountNumber(), request.accountHolderName());
    }

    /**
     * V1 환불계좌 수정 요청 → 수정 커맨드 변환
     *
     * <p>V1에서는 bankName을 사용하여 은행을 식별합니다.
     *
     * @param memberId 회원 ID
     * @param refundAccountId 환불계좌 ID
     * @param request V1 API 요청
     * @return UpdateRefundAccountByBankNameCommand
     */
    public UpdateRefundAccountByBankNameCommand toUpdateCommand(
            UUID memberId, Long refundAccountId, RefundAccountV1ApiRequest request) {
        return UpdateRefundAccountByBankNameCommand.of(
                memberId,
                refundAccountId,
                request.bankName(),
                request.accountNumber(),
                request.accountHolderName());
    }

    /**
     * 환불계좌 삭제 커맨드 생성
     *
     * @param memberId 회원 ID
     * @param refundAccountId 환불계좌 ID
     * @return DeleteRefundAccountCommand
     */
    public DeleteRefundAccountCommand toDeleteCommand(UUID memberId, Long refundAccountId) {
        return DeleteRefundAccountCommand.of(memberId, refundAccountId);
    }

    /**
     * Application Response → V1 API Response 변환
     *
     * <p>V2 Response의 마스킹된 계좌번호를 그대로 사용합니다. (V1 레거시에서는 평문 계좌번호를 반환했으나, 보안상 마스킹 유지)
     *
     * @param response Application 응답
     * @return RefundAccountV1ApiResponse
     */
    public RefundAccountV1ApiResponse toV1Response(RefundAccountResponse response) {
        return new RefundAccountV1ApiResponse(
                response.id(),
                response.bankName(),
                response.maskedAccountNumber(), // V2는 마스킹된 계좌번호 반환
                response.accountHolderName());
    }
}
