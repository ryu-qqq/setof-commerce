package com.ryuqq.setof.application.discountusagehistory.manager.command;

import com.ryuqq.setof.application.discountusagehistory.port.out.command.DiscountUsageHistoryPersistencePort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountUsageHistory;
import com.ryuqq.setof.domain.discount.vo.DiscountUsageHistoryId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 할인 사용 이력 영속성 Manager
 *
 * <p>할인 사용 이력 저장을 위한 Port 호출을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountUsageHistoryPersistenceManager {

    private final DiscountUsageHistoryPersistencePort discountUsageHistoryPersistencePort;

    public DiscountUsageHistoryPersistenceManager(
            DiscountUsageHistoryPersistencePort discountUsageHistoryPersistencePort) {
        this.discountUsageHistoryPersistencePort = discountUsageHistoryPersistencePort;
    }

    /**
     * 할인 사용 이력 저장
     *
     * @param history 저장할 할인 사용 이력
     * @return 저장된 이력 ID
     */
    public DiscountUsageHistoryId persist(DiscountUsageHistory history) {
        return discountUsageHistoryPersistencePort.persist(history);
    }

    /**
     * 할인 사용 이력 일괄 저장
     *
     * @param histories 저장할 할인 사용 이력 목록
     * @return 저장된 이력 ID 목록
     */
    public List<DiscountUsageHistoryId> persistAll(List<DiscountUsageHistory> histories) {
        return discountUsageHistoryPersistencePort.persistAll(histories);
    }
}
