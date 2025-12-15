package com.ryuqq.setof.domain.productstock;

import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import com.ryuqq.setof.domain.productstock.vo.ProductStockId;
import com.ryuqq.setof.domain.productstock.vo.StockQuantity;
import java.time.Instant;

/**
 * ProductStock 테스트 픽스처
 *
 * <p>Object Mother Pattern을 사용하여 테스트 데이터를 생성합니다.
 */
public final class ProductStockFixture {

    private static final Instant DEFAULT_CREATED_AT = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant DEFAULT_UPDATED_AT = Instant.parse("2024-01-01T00:00:00Z");
    private static final Long DEFAULT_VERSION = 0L;

    private ProductStockFixture() {}

    // ========== ProductStock ==========

    /**
     * 기본 ProductStock 생성 (복원용)
     *
     * @return ProductStock 인스턴스
     */
    public static ProductStock createDefault() {
        return ProductStock.reconstitute(
                ProductStockId.of(1L),
                ProductId.of(100L),
                StockQuantity.of(100),
                DEFAULT_VERSION,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 커스텀 ProductStock 생성 (복원용)
     *
     * @param id 재고 ID
     * @param productId 상품 ID
     * @param quantity 재고 수량
     * @return ProductStock 인스턴스
     */
    public static ProductStock createWithQuantity(Long id, Long productId, int quantity) {
        return ProductStock.reconstitute(
                ProductStockId.of(id),
                ProductId.of(productId),
                StockQuantity.of(quantity),
                DEFAULT_VERSION,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 재고 없는 ProductStock 생성
     *
     * @return 재고가 0인 ProductStock 인스턴스
     */
    public static ProductStock createEmpty() {
        return ProductStock.reconstitute(
                ProductStockId.of(1L),
                ProductId.of(100L),
                StockQuantity.zero(),
                DEFAULT_VERSION,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 특정 버전의 ProductStock 생성 (동시성 테스트용)
     *
     * @param id 재고 ID
     * @param productId 상품 ID
     * @param quantity 재고 수량
     * @param version 버전
     * @return ProductStock 인스턴스
     */
    public static ProductStock createWithVersion(
            Long id, Long productId, int quantity, Long version) {
        return ProductStock.reconstitute(
                ProductStockId.of(id),
                ProductId.of(productId),
                StockQuantity.of(quantity),
                version,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 신규 ProductStock 생성
     *
     * @param productId 상품 ID
     * @param quantity 초기 재고 수량
     * @param createdAt 생성일시
     * @return 신규 ProductStock 인스턴스
     */
    public static ProductStock createNew(Long productId, int quantity, Instant createdAt) {
        return ProductStock.create(ProductId.of(productId), StockQuantity.of(quantity), createdAt);
    }

    // ========== ProductStockId ==========

    /**
     * 기본 ProductStockId 생성
     *
     * @return ProductStockId 인스턴스
     */
    public static ProductStockId createDefaultId() {
        return ProductStockId.of(1L);
    }

    /**
     * 커스텀 ProductStockId 생성
     *
     * @param value ID 값
     * @return ProductStockId 인스턴스
     */
    public static ProductStockId createId(Long value) {
        return ProductStockId.of(value);
    }

    // ========== StockQuantity ==========

    /**
     * 기본 StockQuantity 생성
     *
     * @return 100인 StockQuantity 인스턴스
     */
    public static StockQuantity createDefaultQuantity() {
        return StockQuantity.of(100);
    }

    /**
     * 커스텀 StockQuantity 생성
     *
     * @param value 수량 값
     * @return StockQuantity 인스턴스
     */
    public static StockQuantity createQuantity(int value) {
        return StockQuantity.of(value);
    }

    /**
     * 빈 StockQuantity 생성
     *
     * @return 0인 StockQuantity 인스턴스
     */
    public static StockQuantity createEmptyQuantity() {
        return StockQuantity.zero();
    }

    // ========== Instant ==========

    /**
     * 기본 생성일시 반환
     *
     * @return 기본 Instant
     */
    public static Instant defaultCreatedAt() {
        return DEFAULT_CREATED_AT;
    }

    /**
     * 기본 수정일시 반환
     *
     * @return 기본 Instant
     */
    public static Instant defaultUpdatedAt() {
        return DEFAULT_UPDATED_AT;
    }
}
