package com.ryuqq.setof.application.refundaccount.port.in.command;

import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;

/**
 * Delete RefundAccount UseCase (Command)
 *
 * <p>환불계좌 삭제를 담당하는 Inbound Port
 *
 * <p>비즈니스 규칙:
 * <ul>
 *   <li>Soft Delete 적용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteRefundAccountUseCase {

    /**
     * 환불계좌 삭제 실행
     *
     * @param command 환불계좌 삭제 커맨드
     */
    void execute(DeleteRefundAccountCommand command);
}
