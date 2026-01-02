package com.ryuqq.setof.application.common.port.out;

import java.util.List;
import java.util.Map;

/**
 * 재고 캐시-DB 동기화 포트 (출력 포트)
 *
 * <p>Redis 캐시와 RDB 간의 재고 데이터 동기화 전략을 정의합니다.
 *
 * <p><strong>동기화 시나리오:</strong>
 *
 * <ul>
 *   <li><b>Cache Miss</b>: Redis 키 없음 → DB 조회 → 캐시 초기화
 *   <li><b>TTL 만료</b>: 키 자동 삭제 후 재조회 시 동기화
 *   <li><b>강제 동기화</b>: 관리자 요청 또는 정합성 불일치 감지 시
 *   <li><b>캐시 워밍</b>: 시스템 시작 또는 대량 초기화 시
 * </ul>
 *
 * <p><strong>정합성 전략:</strong>
 *
 * <p>Redis는 실시간 카운터로 사용하고, DB는 원천 데이터(Source of Truth)입니다. 불일치 발생 시 DB 값을 기준으로 동기화합니다.
 *
 * <pre>{@code
 * // Cache Miss 처리 패턴
 * int stock = stockCounterPort.getStock(productStockId);
 * if (stock == NOT_FOUND) {
 *     stock = stockCacheSyncPort.syncFromDatabase(productStockId);
 * }
 * }</pre>
 *
 * @author Development Team
 * @since 1.0.0
 */
public interface StockCacheSyncPort {

    /** Cache Miss를 나타내는 상수 */
    int NOT_FOUND = -1;

    /**
     * 단일 상품 재고를 DB에서 캐시로 동기화
     *
     * <p>DB에서 현재 재고를 조회하여 Redis 캐시를 초기화합니다. Cache Miss 발생 시 호출합니다.
     *
     * @param productStockId 상품 재고 ID
     * @return 동기화된 재고 수량, DB에 데이터 없으면 NOT_FOUND
     */
    int syncFromDatabase(Long productStockId);

    /**
     * 복수 상품 재고를 DB에서 캐시로 동기화 (Batch)
     *
     * <p>대량 Cache Miss 발생 시 효율적인 일괄 동기화를 수행합니다.
     *
     * @param productStockIds 상품 재고 ID 목록
     * @return productStockId → 재고수량 매핑, DB에 없으면 NOT_FOUND
     */
    Map<Long, Integer> syncFromDatabase(List<Long> productStockIds);

    /**
     * 캐시 재고를 DB에 반영
     *
     * <p>Redis 카운터 값을 DB에 영속화합니다. 주문 완료, 배치 정산 등의 시점에 호출합니다.
     *
     * @param productStockId 상품 재고 ID
     * @return 동기화 성공 여부
     */
    boolean syncToDatabase(Long productStockId);

    /**
     * 복수 상품 캐시 재고를 DB에 반영 (Batch)
     *
     * @param productStockIds 상품 재고 ID 목록
     * @return 동기화 성공한 ID 목록
     */
    List<Long> syncToDatabase(List<Long> productStockIds);

    /**
     * 캐시-DB 정합성 검증
     *
     * <p>Redis 캐시와 DB 재고의 일치 여부를 확인합니다. 모니터링 및 정합성 검증에 사용합니다.
     *
     * @param productStockId 상품 재고 ID
     * @return 정합성 검증 결과
     */
    StockConsistencyResult verifyConsistency(Long productStockId);

    /**
     * 복수 상품 캐시-DB 정합성 검증 (Batch)
     *
     * @param productStockIds 상품 재고 ID 목록
     * @return 정합성 검증 결과 목록
     */
    List<StockConsistencyResult> verifyConsistency(List<Long> productStockIds);

    /**
     * 캐시 강제 초기화 (DB 기준)
     *
     * <p>정합성 불일치 감지 시 DB 값으로 캐시를 강제 덮어씁니다.
     *
     * @param productStockId 상품 재고 ID
     * @return 강제 동기화된 재고 수량
     */
    int forceSync(Long productStockId);

    /**
     * 캐시 워밍 (시스템 시작용)
     *
     * <p>활성 상품의 재고를 일괄 캐시에 로드합니다. 애플리케이션 시작 또는 캐시 전체 초기화 시 사용합니다.
     *
     * @param sellerId 판매자 ID (null이면 전체)
     * @return 워밍된 상품 수
     */
    int warmCache(Long sellerId);

    /**
     * 정합성 검증 결과
     *
     * @param productStockId 상품 재고 ID
     * @param cacheStock 캐시 재고 (-1이면 캐시 없음)
     * @param databaseStock DB 재고 (-1이면 DB 없음)
     * @param consistent 정합성 여부
     * @param difference 차이 (cache - db)
     */
    record StockConsistencyResult(
            Long productStockId,
            int cacheStock,
            int databaseStock,
            boolean consistent,
            int difference) {

        /**
         * 정합성 검증 결과 생성
         *
         * @param productStockId 상품 재고 ID
         * @param cacheStock 캐시 재고
         * @param databaseStock DB 재고
         * @return 검증 결과
         */
        public static StockConsistencyResult of(
                Long productStockId, int cacheStock, int databaseStock) {
            int diff = cacheStock - databaseStock;
            boolean isConsistent =
                    (cacheStock == databaseStock)
                            || (cacheStock == NOT_FOUND && databaseStock >= 0);
            return new StockConsistencyResult(
                    productStockId, cacheStock, databaseStock, isConsistent, diff);
        }

        /**
         * 캐시 누락 여부
         *
         * @return 캐시에 데이터 없으면 true
         */
        public boolean isCacheMiss() {
            return cacheStock == NOT_FOUND;
        }

        /**
         * DB 데이터 없음 여부
         *
         * @return DB에 데이터 없으면 true
         */
        public boolean isDatabaseMissing() {
            return databaseStock == NOT_FOUND;
        }

        /**
         * 동기화 필요 여부
         *
         * @return 캐시 갱신이 필요하면 true
         */
        public boolean needsSync() {
            return !consistent && databaseStock >= 0;
        }
    }
}
