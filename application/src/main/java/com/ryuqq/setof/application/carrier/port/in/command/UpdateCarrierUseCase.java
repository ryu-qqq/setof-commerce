package com.ryuqq.setof.application.carrier.port.in.command;

import com.ryuqq.setof.application.carrier.dto.command.UpdateCarrierCommand;

/**
 * Update Carrier UseCase (Command)
 *
 * <p>택배사 정보 수정을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateCarrierUseCase {

    /**
     * 택배사 정보 수정 실행
     *
     * @param carrierId 택배사 ID
     * @param command 택배사 수정 명령
     */
    void execute(Long carrierId, UpdateCarrierCommand command);
}
