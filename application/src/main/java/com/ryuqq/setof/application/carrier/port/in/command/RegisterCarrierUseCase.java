package com.ryuqq.setof.application.carrier.port.in.command;

import com.ryuqq.setof.application.carrier.dto.command.RegisterCarrierCommand;

/**
 * Register Carrier UseCase (Command)
 *
 * <p>택배사 등록을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterCarrierUseCase {

    /**
     * 택배사 등록 실행
     *
     * @param command 택배사 등록 명령
     * @return 등록된 택배사 ID
     */
    Long execute(RegisterCarrierCommand command);
}
