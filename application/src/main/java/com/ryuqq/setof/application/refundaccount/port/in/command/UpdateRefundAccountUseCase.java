package com.ryuqq.setof.application.refundaccount.port.in.command;

import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;

/**
 * Update RefundAccount UseCase (Command)
 *
 * <p>환불계좌 수정을 담당하는 Inbound Port
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>수정 시 외부 계좌 검증 API 통해 재검증 필수
 *   <li>검증 실패 시 수정 불가
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateRefundAccountUseCase {

    /**
     * 환불계좌 수정 실행
     *
     * @param command 환불계좌 수정 커맨드
     * @return 수정된 환불계좌 정보
     */
    RefundAccountResponse execute(UpdateRefundAccountCommand command);
}
