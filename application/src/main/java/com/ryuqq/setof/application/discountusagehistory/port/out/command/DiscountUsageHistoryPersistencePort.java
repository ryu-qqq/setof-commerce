package com.ryuqq.setof.application.discountusagehistory.port.out.command;

import com.ryuqq.setof.domain.discount.aggregate.DiscountUsageHistory;
import com.ryuqq.setof.domain.discount.vo.DiscountUsageHistoryId;
import java.util.List;

/**
 * 할인 사용 이력 저장 Port
 *
 * <p>Application Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Domain 객체 직접 사용 - DTO 변환 없음
 *   <li>Long FK 전략 - JPA 관계 어노테이션 사용 안함
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DiscountUsageHistoryPersistencePort {

    /**
     * 할인 사용 이력 저장
     *
     * @param history 저장할 할인 사용 이력
     * @return 저장된 이력 ID
     */
    DiscountUsageHistoryId persist(DiscountUsageHistory history);

    /**
     * 할인 사용 이력 일괄 저장
     *
     * @param histories 저장할 할인 사용 이력 목록
     * @return 저장된 이력 ID 목록
     */
    List<DiscountUsageHistoryId> persistAll(List<DiscountUsageHistory> histories);
}
