package com.ryuqq.setof.application.stock.manager;

import com.ryuqq.setof.application.common.port.out.DistributedLockPort;
import com.ryuqq.setof.application.stock.port.out.command.StockCommandPort;
import com.ryuqq.setof.application.stock.port.out.query.StockQueryPort;
import com.ryuqq.setof.domain.stock.exception.InsufficientStockException;
import com.ryuqq.setof.domain.stock.vo.StockDeductionItem;
import com.ryuqq.setof.domain.stock.vo.StockLockKey;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * 재고 차감 Manager.
 *
 * <p>Redisson 분산 락 + DB 직접 차감 방식으로 재고를 관리합니다.
 *
 * <p>productId 오름차순으로 락을 잡아 데드락을 방지합니다. 중간에 실패하면 이미 차감한 재고를 보상 복원합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class StockDeductionManager {

    private static final long LOCK_WAIT_TIME = 5;
    private static final long LOCK_LEASE_TIME = 10;

    private final DistributedLockPort distributedLockPort;
    private final StockQueryPort stockQueryPort;
    private final StockCommandPort stockCommandPort;

    public StockDeductionManager(
            DistributedLockPort distributedLockPort,
            StockQueryPort stockQueryPort,
            StockCommandPort stockCommandPort) {
        this.distributedLockPort = distributedLockPort;
        this.stockQueryPort = stockQueryPort;
        this.stockCommandPort = stockCommandPort;
    }

    /**
     * 주문 항목들의 재고를 일괄 차감합니다.
     *
     * <p>productId 오름차순 정렬 → 분산 락 획득 → 재고 검증 → 차감 → 락 해제. 중간에 실패하면 이미 차감한 항목들을 복원합니다.
     *
     * @param items 차감할 항목 목록
     * @throws InsufficientStockException 재고 부족 시
     */
    public void deductAll(List<StockDeductionItem> items) {
        List<StockDeductionItem> sorted =
                items.stream()
                        .sorted(Comparator.comparingLong(StockDeductionItem::productId))
                        .toList();

        List<StockDeductionItem> deducted = new ArrayList<>();

        try {
            for (StockDeductionItem item : sorted) {
                deductSingle(item);
                deducted.add(item);
            }
        } catch (Exception e) {
            restoreAll(deducted);
            throw e;
        }
    }

    /**
     * 재고를 복원합니다 (취소/반품 시).
     *
     * <p>복원은 항상 안전하므로 락 없이 수행합니다.
     *
     * @param items 복원할 항목 목록
     */
    public void restoreAll(List<StockDeductionItem> items) {
        for (StockDeductionItem item : items) {
            stockCommandPort.restore(item.productId(), item.quantity());
        }
    }

    private void deductSingle(StockDeductionItem item) {
        StockLockKey lockKey = new StockLockKey(item.productId());
        boolean acquired =
                distributedLockPort.tryLock(
                        lockKey, LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

        if (!acquired) {
            throw new IllegalStateException("재고 락 획득 실패: productId=" + item.productId());
        }

        try {
            int currentStock = stockQueryPort.getQuantity(item.productId());
            if (currentStock < item.quantity()) {
                throw new InsufficientStockException(item.productId());
            }
            stockCommandPort.deduct(item.productId(), item.quantity());
        } finally {
            distributedLockPort.unlock(lockKey);
        }
    }
}
