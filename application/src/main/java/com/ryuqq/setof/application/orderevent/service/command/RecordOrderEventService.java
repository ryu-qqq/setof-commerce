package com.ryuqq.setof.application.orderevent.service.command;

import com.ryuqq.setof.application.orderevent.assembler.OrderEventAssembler;
import com.ryuqq.setof.application.orderevent.dto.command.RecordOrderEventCommand;
import com.ryuqq.setof.application.orderevent.dto.response.OrderEventResponse;
import com.ryuqq.setof.application.orderevent.port.in.command.RecordOrderEventUseCase;
import com.ryuqq.setof.application.orderevent.port.out.command.OrderEventPersistencePort;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.orderevent.aggregate.OrderEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RecordOrderEventService - 주문 이벤트 기록 서비스
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class RecordOrderEventService implements RecordOrderEventUseCase {

    private final OrderEventPersistencePort orderEventPersistencePort;
    private final OrderEventAssembler orderEventAssembler;
    private final ClockHolder clockHolder;

    public RecordOrderEventService(
            OrderEventPersistencePort orderEventPersistencePort,
            OrderEventAssembler orderEventAssembler,
            ClockHolder clockHolder) {
        this.orderEventPersistencePort = orderEventPersistencePort;
        this.orderEventAssembler = orderEventAssembler;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public OrderEventResponse recordEvent(RecordOrderEventCommand command) {
        OrderEvent orderEvent =
                OrderEvent.create(
                        command.orderId(),
                        command.eventType(),
                        command.eventSource(),
                        command.sourceId(),
                        command.previousStatus(),
                        command.currentStatus(),
                        command.actorType(),
                        command.actorId(),
                        command.description(),
                        command.metadata(),
                        clockHolder.getClock().instant());

        OrderEvent saved = orderEventPersistencePort.persist(orderEvent);
        return orderEventAssembler.toResponse(saved);
    }
}
