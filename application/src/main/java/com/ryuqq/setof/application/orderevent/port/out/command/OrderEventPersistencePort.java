package com.ryuqq.setof.application.orderevent.port.out.command;

import com.ryuqq.setof.domain.orderevent.aggregate.OrderEvent;

/**
 * OrderEventPersistencePort - OrderEvent 영속성 Port
 *
 * <p>OrderEvent 저장을 위한 Outbound Port입니다.
 *
 * <p>규칙:
 *
 * <ul>
 *   <li>persist(): 저장/수정 통합 (JPA merge 활용 - ID 유무로 INSERT/UPDATE 분기)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public interface OrderEventPersistencePort {

    /**
     * OrderEvent 저장 (persist 패턴)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param orderEvent 저장할 이벤트
     * @return 저장된 이벤트
     */
    OrderEvent persist(OrderEvent orderEvent);
}
