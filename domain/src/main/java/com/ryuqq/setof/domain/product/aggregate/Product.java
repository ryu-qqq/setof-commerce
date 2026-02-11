package com.ryuqq.setof.domain.product.aggregate;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;

/**
 * Product - 상품(SKU) Aggregate Root.
 *
 * <p>개별 SKU 정보를 관리합니다. 옵션, 추가 가격, 재고를 내장합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>productGroupId는 필수
 *   <li>stockQuantity >= 0
 *   <li>additionalPrice >= 0
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class Product {

    private final ProductId id;
    private final ProductGroupId productGroupId;
    private String option1Name;
    private String option1Value;
    private String option2Name;
    private String option2Value;
    private Money additionalPrice;
    private int stockQuantity;
    private ProductStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Product(
            ProductId id,
            ProductGroupId productGroupId,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Money additionalPrice,
            int stockQuantity,
            ProductStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.option1Name = option1Name;
        this.option1Value = option1Value;
        this.option2Name = option2Name;
        this.option2Value = option2Value;
        this.additionalPrice = additionalPrice;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 상품(SKU) 생성.
     *
     * @param productGroupId 상품그룹 ID (필수)
     * @param option1Name 옵션1 이름 (예: "색상")
     * @param option1Value 옵션1 값 (예: "블랙")
     * @param option2Name 옵션2 이름 (예: "사이즈")
     * @param option2Value 옵션2 값 (예: "M")
     * @param additionalPrice 옵션 추가금
     * @param stockQuantity 초기 재고
     * @param now 생성 시각
     * @return 새 Product 인스턴스 (ACTIVE 상태)
     */
    public static Product forNew(
            ProductGroupId productGroupId,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Money additionalPrice,
            int stockQuantity,
            Instant now) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다");
        }
        return new Product(
                ProductId.forNew(),
                productGroupId,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                additionalPrice != null ? additionalPrice : Money.zero(),
                stockQuantity,
                ProductStatus.ACTIVE,
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param productGroupId 상품그룹 ID
     * @param option1Name 옵션1 이름
     * @param option1Value 옵션1 값
     * @param option2Name 옵션2 이름
     * @param option2Value 옵션2 값
     * @param additionalPrice 옵션 추가금
     * @param stockQuantity 재고 수량
     * @param status 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 Product 인스턴스
     */
    public static Product reconstitute(
            ProductId id,
            ProductGroupId productGroupId,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Money additionalPrice,
            int stockQuantity,
            ProductStatus status,
            Instant createdAt,
            Instant updatedAt) {
        return new Product(
                id,
                productGroupId,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                additionalPrice,
                stockQuantity,
                status,
                createdAt,
                updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 재고 수량 업데이트.
     *
     * @param quantity 새 재고 수량 (0 이상)
     * @param now 수정 시각
     */
    public void updateStock(int quantity, Instant now) {
        if (quantity < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다");
        }
        this.stockQuantity = quantity;
        this.updatedAt = now;
        if (quantity == 0 && status.canMarkSoldOut()) {
            this.status = ProductStatus.SOLDOUT;
        }
    }

    /**
     * 품절 처리.
     *
     * @param now 품절 시각
     */
    public void markSoldOut(Instant now) {
        if (!status.canMarkSoldOut()) {
            throw new IllegalStateException(String.format("상태 %s에서 품절 처리할 수 없습니다", status));
        }
        this.status = ProductStatus.SOLDOUT;
        this.stockQuantity = 0;
        this.updatedAt = now;
    }

    /**
     * 활성화.
     *
     * @param now 활성화 시각
     */
    public void activate(Instant now) {
        if (!status.canActivate()) {
            throw new IllegalStateException(String.format("상태 %s에서 활성화할 수 없습니다", status));
        }
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = now;
    }

    /**
     * 비활성화.
     *
     * @param now 비활성화 시각
     */
    public void deactivate(Instant now) {
        if (!status.canDeactivate()) {
            throw new IllegalStateException(String.format("상태 %s에서 비활성화할 수 없습니다", status));
        }
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = now;
    }

    /**
     * 삭제 처리.
     *
     * @param now 삭제 시각
     */
    public void delete(Instant now) {
        if (!status.canDelete()) {
            throw new IllegalStateException("이미 삭제된 상품입니다");
        }
        this.status = ProductStatus.DELETED;
        this.updatedAt = now;
    }

    /**
     * 옵션 정보 수정.
     *
     * @param option1Name 옵션1 이름
     * @param option1Value 옵션1 값
     * @param option2Name 옵션2 이름
     * @param option2Value 옵션2 값
     * @param now 수정 시각
     */
    public void updateOptions(
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Instant now) {
        this.option1Name = option1Name;
        this.option1Value = option1Value;
        this.option2Name = option2Name;
        this.option2Value = option2Value;
        this.updatedAt = now;
    }

    /** 재고 보유 여부 확인 */
    public boolean hasStock() {
        return stockQuantity > 0;
    }

    /** 품절 상태 확인 */
    public boolean isSoldOut() {
        return status.isSoldOut();
    }

    /** 활성 상태 확인 */
    public boolean isActive() {
        return status.isActive();
    }

    /** 삭제 상태 확인 */
    public boolean isDeleted() {
        return status.isDeleted();
    }

    /** 옵션 보유 여부 확인 */
    public boolean hasOptions() {
        return option1Name != null && !option1Name.isBlank();
    }

    /** 조합 옵션 보유 여부 확인 */
    public boolean hasCombinationOptions() {
        return hasOptions() && option2Name != null && !option2Name.isBlank();
    }

    // ========== Accessor Methods ==========

    public ProductId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public String option1Name() {
        return option1Name;
    }

    public String option1Value() {
        return option1Value;
    }

    public String option2Name() {
        return option2Name;
    }

    public String option2Value() {
        return option2Value;
    }

    public Money additionalPrice() {
        return additionalPrice;
    }

    public int additionalPriceValue() {
        return additionalPrice != null ? additionalPrice.value() : 0;
    }

    public int stockQuantity() {
        return stockQuantity;
    }

    public ProductStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
