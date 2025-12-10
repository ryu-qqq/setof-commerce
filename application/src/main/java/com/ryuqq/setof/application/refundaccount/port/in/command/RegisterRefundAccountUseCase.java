package com.ryuqq.setof.application.refundaccount.port.in.command;

import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;

/**
 * Register RefundAccount UseCase (Command)
 *
 * <p>환불계좌 등록을 담당하는 Inbound Port
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>회원당 최대 1개까지만 등록 가능
 *   <li>등록 시 외부 계좌 검증 API 통해 검증 필수
 *   <li>검증 실패 시 저장 불가
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterRefundAccountUseCase {

    /**
     * 환불계좌 등록 실행
     *
     * @param command 환불계좌 등록 커맨드
     * @return 등록된 환불계좌 정보
     */
    RefundAccountResponse execute(RegisterRefundAccountCommand command);
}
