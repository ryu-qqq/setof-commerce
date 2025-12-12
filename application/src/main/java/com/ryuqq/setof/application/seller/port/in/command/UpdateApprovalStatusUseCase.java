package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;

/**
 * Update Approval Status UseCase (Command)
 *
 * <p>셀러 승인 상태 변경을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateApprovalStatusUseCase {

    /**
     * 셀러 승인 상태 변경 실행
     *
     * @param command 승인 상태 변경 명령
     */
    void execute(UpdateApprovalStatusCommand command);
}
