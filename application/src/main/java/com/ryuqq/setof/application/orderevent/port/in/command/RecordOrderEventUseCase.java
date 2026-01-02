package com.ryuqq.setof.application.orderevent.port.in.command;

import com.ryuqq.setof.application.orderevent.dto.command.RecordOrderEventCommand;
import com.ryuqq.setof.application.orderevent.dto.response.OrderEventResponse;

/**
 * RecordOrderEventUseCase - 주문 이벤트 기록 UseCase
 *
 * @author development-team
 * @since 2.0.0
 */
public interface RecordOrderEventUseCase {

    /**
     * 주문 이벤트 기록
     *
     * @param command 이벤트 기록 커맨드
     * @return 기록된 이벤트 응답
     */
    OrderEventResponse recordEvent(RecordOrderEventCommand command);
}
