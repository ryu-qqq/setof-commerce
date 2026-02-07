package com.ryuqq.setof.application.selleradmin.port.in.command;

import com.ryuqq.setof.application.common.dto.result.SchedulerBatchProcessingResult;
import com.ryuqq.setof.application.selleradmin.dto.command.ProcessPendingSellerAdminOutboxCommand;

/**
 * 대기 중인 셀러 관리자 인증 Outbox 처리 UseCase.
 *
 * <p>PENDING 상태이면서 재시도 횟수가 남아있는 Outbox를 처리합니다.
 */
public interface ProcessPendingSellerAdminOutboxUseCase {

    SchedulerBatchProcessingResult execute(ProcessPendingSellerAdminOutboxCommand command);
}
