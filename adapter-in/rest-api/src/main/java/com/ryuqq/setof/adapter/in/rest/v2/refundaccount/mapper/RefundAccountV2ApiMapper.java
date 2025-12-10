package com.ryuqq.setof.adapter.in.rest.v2.refundaccount.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.RegisterRefundAccountV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.UpdateRefundAccountV2ApiRequest;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * RefundAccount V2 API Mapper
 *
 * <p>환불계좌 관련 API DTO ↔ Application Command 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class RefundAccountV2ApiMapper {

    /**
     * 환불계좌 등록 요청 → 등록 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param request API 요청
     * @return RegisterRefundAccountCommand
     */
    public RegisterRefundAccountCommand toRegisterCommand(
            UUID memberId, RegisterRefundAccountV2ApiRequest request) {
        return RegisterRefundAccountCommand.of(
                memberId, request.bankId(), request.accountNumber(), request.accountHolderName());
    }

    /**
     * 환불계좌 수정 요청 → 수정 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param refundAccountId 환불계좌 ID
     * @param request API 요청
     * @return UpdateRefundAccountCommand
     */
    public UpdateRefundAccountCommand toUpdateCommand(
            UUID memberId, Long refundAccountId, UpdateRefundAccountV2ApiRequest request) {
        return UpdateRefundAccountCommand.of(
                memberId,
                refundAccountId,
                request.bankId(),
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
}
