package com.ryuqq.setof.application.common.port.out;

import java.util.List;
import java.util.Map;

/**
 * 재고 분산 카운터 포트 (출력 포트)
 *
 * <p>Redis 기반 분산 재고 카운터 추상화입니다. DECRBY/INCRBY 명령어를 사용하여 원자적(atomic) 재고 연산을 수행합니다.
 *
 * <p><strong>사용 패턴:</strong>
 *
 * <pre>{@code
 * // 재고 차감 (결제 완료 시)
 * int remaining = stockCounterPort.decrement(productStockId, quantity);
 * if (remaining < 0) {
 *     stockCounterPort.increment(productStockId, quantity); // 롤백
 *     throw new InsufficientStockException(productStockId);
 * }
 *
 * // 재고 복구 (주문 취소 시)
 * stockCounterPort.increment(productStockId, quantity);
 * }</pre>
 *
 * <p><strong>키 형식:</strong> {@code stock:counter:{productStockId}}
 *
 * <p><strong>주의사항:</strong>
 *
 * <ul>
 *   <li>Redis 카운터와 RDB 재고는 비동기 동기화됨
 *   <li>Redis 장애 시 RDB 폴백 로직 필요
 *   <li>음수 재고 발생 시 즉시 롤백 필요
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 */
public interface StockCounterPort {

    /**
     * 재고 차감 (Atomic DECRBY)
     *
     * <p>원자적으로 재고를 차감합니다. 반환값이 음수인 경우 재고 부족이므로 롤백이 필요합니다.
     *
     * <p><strong>사용 예시:</strong>
     *
     * <pre>{@code
     * int remaining = stockCounterPort.decrement(productStockId, 2);
     * if (remaining < 0) {
     *     stockCounterPort.increment(productStockId, 2); // 롤백
     *     throw new InsufficientStockException(productStockId);
     * }
     * }</pre>
     *
     * @param productStockId 상품 재고 ID
     * @param quantity 차감할 수량
     * @return 차감 후 남은 수량 (음수 가능)
     */
    int decrement(Long productStockId, int quantity);

    /**
     * 재고 증가 (Atomic INCRBY)
     *
     * <p>원자적으로 재고를 증가합니다. 주문 취소, 롤백 시 사용합니다.
     *
     * @param productStockId 상품 재고 ID
     * @param quantity 증가할 수량
     * @return 증가 후 수량
     */
    int increment(Long productStockId, int quantity);

    /**
     * 현재 재고 수량 조회
     *
     * @param productStockId 상품 재고 ID
     * @return 현재 재고 수량 (키 없으면 -1 반환)
     */
    int getStock(Long productStockId);

    /**
     * 복수 상품 재고 수량 조회 (Batch)
     *
     * @param productStockIds 상품 재고 ID 목록
     * @return productStockId → 재고수량 매핑 (키 없으면 -1)
     */
    Map<Long, Integer> getStocks(List<Long> productStockIds);

    /**
     * 재고 충분 여부 확인
     *
     * @param productStockId 상품 재고 ID
     * @param requiredQuantity 필요 수량
     * @return 재고가 충분하면 true
     */
    boolean hasStock(Long productStockId, int requiredQuantity);

    /**
     * 복수 상품 재고 충분 여부 확인 (Batch)
     *
     * <p>하나라도 재고 부족이면 false 반환
     *
     * @param stockRequirements productStockId → 필요수량 매핑
     * @return 모든 상품 재고가 충분하면 true
     */
    boolean hasStocks(Map<Long, Integer> stockRequirements);

    /**
     * 재고 초기화 (RDB → Redis 동기화)
     *
     * <p>Redis 키가 없거나 RDB와 동기화가 필요할 때 호출합니다.
     *
     * @param productStockId 상품 재고 ID
     * @param quantity 초기 재고 수량 (RDB 값)
     */
    void initialize(Long productStockId, int quantity);

    /**
     * 복수 상품 재고 초기화 (Batch)
     *
     * @param stocks productStockId → 초기수량 매핑
     */
    void initializeAll(Map<Long, Integer> stocks);

    /**
     * 재고 키 존재 여부 확인
     *
     * @param productStockId 상품 재고 ID
     * @return Redis에 키가 존재하면 true
     */
    boolean exists(Long productStockId);

    /**
     * 재고 키 삭제
     *
     * <p>상품 삭제 시 Redis 키도 정리합니다.
     *
     * @param productStockId 상품 재고 ID
     */
    void delete(Long productStockId);
}
