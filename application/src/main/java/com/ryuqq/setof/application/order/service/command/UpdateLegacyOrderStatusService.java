package com.ryuqq.setof.application.order.service.command;

import com.ryuqq.setof.application.order.dto.command.UpdateLegacyOrderStatusCommand;
import com.ryuqq.setof.application.order.dto.response.UpdateOrderResult;
import com.ryuqq.setof.application.order.manager.OrderCommandManager;
import com.ryuqq.setof.application.order.port.in.command.UpdateLegacyOrderStatusUseCase;
import org.springframework.stereotype.Service;

/**
 * UpdateLegacyOrderStatusService - Legacy 주문 상태 변경 Service.
 *
 * <p>Legacy orders 테이블의 order_status 컬럼을 직접 업데이트합니다. 취소 철회(CANCEL_REQUEST_RECANT) 시
 * ORDER_COMPLETED로, 반품 철회(RETURN_REQUEST_RECANT) 시 DELIVERY_COMPLETED로 강제 전이합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateLegacyOrderStatusService implements UpdateLegacyOrderStatusUseCase {

    private static final String CANCEL_REQUEST_RECANT = "CANCEL_REQUEST_RECANT";
    private static final String RETURN_REQUEST_RECANT = "RETURN_REQUEST_RECANT";
    private static final String ORDER_COMPLETED = "ORDER_COMPLETED";
    private static final String DELIVERY_COMPLETED = "DELIVERY_COMPLETED";

    private final OrderCommandManager commandManager;

    public UpdateLegacyOrderStatusService(OrderCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public UpdateOrderResult execute(UpdateLegacyOrderStatusCommand command) {
        String resolvedTarget = resolveTargetStatus(command.targetOrderStatus());
        String asIsStatus = commandManager.updateOrderStatus(command.orderId(), resolvedTarget);

        return new UpdateOrderResult(
                command.orderId(),
                command.userId(),
                resolvedTarget,
                asIsStatus,
                command.changeReason(),
                command.changeDetailReason());
    }

    private String resolveTargetStatus(String targetOrderStatus) {
        return switch (targetOrderStatus) {
            case CANCEL_REQUEST_RECANT -> ORDER_COMPLETED;
            case RETURN_REQUEST_RECANT -> DELIVERY_COMPLETED;
            default -> targetOrderStatus;
        };
    }
}
