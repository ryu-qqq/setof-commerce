package com.ryuqq.setof.domain.productstock.aggregate;

import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.vo.ProductStockId;
import com.ryuqq.setof.domain.productstock.vo.StockQuantity;
import java.time.Instant;
import java.util.Objects;

/**
 * ProductStock Aggregate Root
 *
 * <p>상품 재고를 관리하는 도메인 엔티티입니다. Product와 분리하여 동시성 처리를 최적화합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
public class ProductStock {

    private final ProductStockId id;
    private final ProductId productId;
    private final StockQuantity quantity;
    private final Long version;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private ProductStock(
            ProductStockId id,
            ProductId productId,
            StockQuantity quantity,
            Long version,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 재고 생성용 Static Factory Method
     *
     * <p>ID 없이 신규 생성 (version은 null로 시작, JPA가 0으로 초기화)
     *
     * @param productId 상품 ID
     * @param quantity 초기 재고 수량
     * @param createdAt 생성일시
     * @return ProductStock 인스턴스
     */
    public static ProductStock create(
            ProductId productId, StockQuantity quantity, Instant createdAt) {
        validateCreate(productId, quantity);
        return new ProductStock(
                ProductStockId.forNew(), productId, quantity, null, createdAt, createdAt);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 재고 ID
     * @param productId 상품 ID
     * @param quantity 재고 수량
     * @param version 낙관적 락 버전
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return ProductStock 인스턴스
     */
    public static ProductStock reconstitute(
            ProductStockId id,
            ProductId productId,
            StockQuantity quantity,
            Long version,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductStock(id, productId, quantity, version, createdAt, updatedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 재고 차감
     *
     * <p>version을 보존하여 새 인스턴스 생성 (낙관적 락 지원)
     *
     * @param deductQuantity 차감할 수량
     * @param now 현재 시각
     * @return 차감된 새로운 ProductStock 인스턴스
     */
    public ProductStock deduct(int deductQuantity, Instant now) {
        StockQuantity newQuantity = quantity.deduct(deductQuantity, productId.value());
        return new ProductStock(id, productId, newQuantity, version, createdAt, now);
    }

    /**
     * 재고 복원
     *
     * <p>version을 보존하여 새 인스턴스 생성 (낙관적 락 지원)
     *
     * @param restoreQuantity 복원할 수량
     * @param now 현재 시각
     * @return 복원된 새로운 ProductStock 인스턴스
     */
    public ProductStock restore(int restoreQuantity, Instant now) {
        StockQuantity newQuantity = quantity.restore(restoreQuantity, productId.value());
        return new ProductStock(id, productId, newQuantity, version, createdAt, now);
    }

    /**
     * 재고 수량 설정
     *
     * <p>version을 보존하여 새 인스턴스 생성 (낙관적 락 지원)
     *
     * @param newQuantity 새로운 재고 수량
     * @param now 현재 시각
     * @return 수량이 변경된 새로운 ProductStock 인스턴스
     */
    public ProductStock setQuantity(int newQuantity, Instant now) {
        StockQuantity updatedQuantity = StockQuantity.of(newQuantity);
        return new ProductStock(id, productId, updatedQuantity, version, createdAt, now);
    }

    /**
     * 재고 가용 여부 확인 (Tell, Don't Ask)
     *
     * @param requestedQuantity 요청 수량
     * @return 재고가 충분하면 true
     */
    public boolean isAvailable(int requestedQuantity) {
        return quantity.isEnough(requestedQuantity);
    }

    /**
     * 재고 존재 여부 확인
     *
     * @return 재고가 있으면 true
     */
    public boolean hasStock() {
        return quantity.hasStock();
    }

    /**
     * 재고 없음 여부 확인
     *
     * @return 재고가 없으면 true
     */
    public boolean isEmpty() {
        return quantity.isEmpty();
    }

    /**
     * 신규 생성 여부 확인
     *
     * @return ID가 null이면 true (아직 영속화되지 않은 신규 엔티티)
     */
    public boolean isNew() {
        return id.isNew();
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 재고 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 재고 ID Long 값
     */
    public Long getIdValue() {
        return id.value();
    }

    /**
     * 상품 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품 ID Long 값
     */
    public Long getProductIdValue() {
        return productId.value();
    }

    /**
     * 재고 수량 값 반환 (Law of Demeter 준수)
     *
     * @return 재고 수량 int 값
     */
    public int getQuantityValue() {
        return quantity.value();
    }

    /**
     * 낙관적 락 버전 값 반환 (Law of Demeter 준수)
     *
     * @return 버전 Long 값 (신규 생성 시 null)
     */
    public Long getVersionValue() {
        return version;
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ProductStockId getId() {
        return id;
    }

    public ProductId getProductId() {
        return productId;
    }

    public StockQuantity getQuantity() {
        return quantity;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // ========== Private Validation ==========

    private static void validateCreate(ProductId productId, StockQuantity quantity) {
        Objects.requireNonNull(productId, "상품 ID는 필수입니다");
        Objects.requireNonNull(quantity, "재고 수량은 필수입니다");
    }
}
