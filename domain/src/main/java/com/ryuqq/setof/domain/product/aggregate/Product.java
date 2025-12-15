package com.ryuqq.setof.domain.product.aggregate;

import com.ryuqq.setof.domain.product.vo.Money;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import java.time.Instant;

/**
 * Product Aggregate (SKU 단위)
 *
 * <p>개별 SKU 정보를 나타내는 도메인 엔티티입니다. ProductGroup에 속합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 *
 * <p>옵션 구성:
 *
 * <ul>
 *   <li>SINGLE: 옵션 없음 (option1/option2 모두 null)
 *   <li>ONE_LEVEL: 1단 옵션 (option1만 사용)
 *   <li>TWO_LEVEL: 2단 옵션 (option1, option2 모두 사용)
 * </ul>
 */
public class Product {

    private final ProductId id;
    private final ProductGroupId productGroupId;
    private final OptionType optionType;
    private final String option1Name;
    private final String option1Value;
    private final String option2Name;
    private final String option2Value;
    private final Money additionalPrice;
    private final ProductStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Product(
            ProductId id,
            ProductGroupId productGroupId,
            OptionType optionType,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Money additionalPrice,
            ProductStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.optionType = optionType;
        this.option1Name = option1Name;
        this.option1Value = option1Value;
        this.option2Name = option2Name;
        this.option2Value = option2Value;
        this.additionalPrice = additionalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 단품 SKU 생성 (옵션 없음)
     *
     * @param productGroupId 상품그룹 ID
     * @param additionalPrice 추가 금액
     * @param createdAt 생성일시
     * @return Product 인스턴스
     */
    public static Product createSingle(
            ProductGroupId productGroupId, Money additionalPrice, Instant createdAt) {
        validateProductGroupId(productGroupId);
        return new Product(
                ProductId.forNew(),
                productGroupId,
                OptionType.SINGLE,
                null,
                null,
                null,
                null,
                additionalPrice != null ? additionalPrice : Money.zero(),
                ProductStatus.defaultStatus(),
                createdAt,
                createdAt,
                null);
    }

    /**
     * 1단 옵션 SKU 생성
     *
     * @param productGroupId 상품그룹 ID
     * @param option1Name 옵션1 이름 (예: "색상")
     * @param option1Value 옵션1 값 (예: "블랙")
     * @param additionalPrice 추가 금액
     * @param createdAt 생성일시
     * @return Product 인스턴스
     */
    public static Product createOneLevel(
            ProductGroupId productGroupId,
            String option1Name,
            String option1Value,
            Money additionalPrice,
            Instant createdAt) {
        validateProductGroupId(productGroupId);
        validateOption1(option1Name, option1Value);
        return new Product(
                ProductId.forNew(),
                productGroupId,
                OptionType.ONE_LEVEL,
                option1Name,
                option1Value,
                null,
                null,
                additionalPrice != null ? additionalPrice : Money.zero(),
                ProductStatus.defaultStatus(),
                createdAt,
                createdAt,
                null);
    }

    /**
     * 2단 옵션 SKU 생성
     *
     * @param productGroupId 상품그룹 ID
     * @param option1Name 옵션1 이름 (예: "색상")
     * @param option1Value 옵션1 값 (예: "블랙")
     * @param option2Name 옵션2 이름 (예: "사이즈")
     * @param option2Value 옵션2 값 (예: "M")
     * @param additionalPrice 추가 금액
     * @param createdAt 생성일시
     * @return Product 인스턴스
     */
    public static Product createTwoLevel(
            ProductGroupId productGroupId,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Money additionalPrice,
            Instant createdAt) {
        validateProductGroupId(productGroupId);
        validateOption1(option1Name, option1Value);
        validateOption2(option2Name, option2Value);
        return new Product(
                ProductId.forNew(),
                productGroupId,
                OptionType.TWO_LEVEL,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                additionalPrice != null ? additionalPrice : Money.zero(),
                ProductStatus.defaultStatus(),
                createdAt,
                createdAt,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 상품 ID
     * @param productGroupId 상품그룹 ID
     * @param optionType 옵션 타입
     * @param option1Name 옵션1 이름
     * @param option1Value 옵션1 값
     * @param option2Name 옵션2 이름
     * @param option2Value 옵션2 값
     * @param additionalPrice 추가 금액
     * @param status 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @return Product 인스턴스
     */
    public static Product reconstitute(
            ProductId id,
            ProductGroupId productGroupId,
            OptionType optionType,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Money additionalPrice,
            ProductStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new Product(
                id,
                productGroupId,
                optionType,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                additionalPrice,
                status,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 옵션 정보 업데이트
     *
     * @param option1Name 새로운 옵션1 이름
     * @param option1Value 새로운 옵션1 값
     * @param option2Name 새로운 옵션2 이름
     * @param option2Value 새로운 옵션2 값
     * @param additionalPrice 새로운 추가 금액
     * @param updatedAt 수정일시
     * @return 업데이트된 Product 인스턴스
     */
    public Product updateOption(
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            Money additionalPrice,
            Instant updatedAt) {
        return new Product(
                this.id,
                this.productGroupId,
                this.optionType,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                additionalPrice != null ? additionalPrice : Money.zero(),
                this.status,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 품절 처리
     *
     * @param updatedAt 수정일시
     * @return 품절 상태의 Product 인스턴스
     */
    public Product markSoldOut(Instant updatedAt) {
        return new Product(
                this.id,
                this.productGroupId,
                this.optionType,
                this.option1Name,
                this.option1Value,
                this.option2Name,
                this.option2Value,
                this.additionalPrice,
                this.status.markSoldOut(),
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 재고 복원 (품절 해제)
     *
     * @param updatedAt 수정일시
     * @return 재고 있음 상태의 Product 인스턴스
     */
    public Product markInStock(Instant updatedAt) {
        return new Product(
                this.id,
                this.productGroupId,
                this.optionType,
                this.option1Name,
                this.option1Value,
                this.option2Name,
                this.option2Value,
                this.additionalPrice,
                this.status.markInStock(),
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 노출 처리
     *
     * @param updatedAt 수정일시
     * @return 노출 상태의 Product 인스턴스
     */
    public Product show(Instant updatedAt) {
        return new Product(
                this.id,
                this.productGroupId,
                this.optionType,
                this.option1Name,
                this.option1Value,
                this.option2Name,
                this.option2Value,
                this.additionalPrice,
                this.status.show(),
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 숨김 처리
     *
     * @param updatedAt 수정일시
     * @return 숨김 상태의 Product 인스턴스
     */
    public Product hide(Instant updatedAt) {
        return new Product(
                this.id,
                this.productGroupId,
                this.optionType,
                this.option1Name,
                this.option1Value,
                this.option2Name,
                this.option2Value,
                this.additionalPrice,
                this.status.hide(),
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 삭제 처리 (Soft Delete)
     *
     * @param deletedAt 삭제일시
     * @return 삭제된 Product 인스턴스
     */
    public Product delete(Instant deletedAt) {
        return new Product(
                this.id,
                this.productGroupId,
                this.optionType,
                this.option1Name,
                this.option1Value,
                this.option2Name,
                this.option2Value,
                this.additionalPrice,
                this.status.hide(),
                this.createdAt,
                deletedAt,
                deletedAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 품절 여부 확인
     *
     * @return 품절이면 true
     */
    public boolean isSoldOut() {
        return status.soldOut();
    }

    /**
     * 노출 여부 확인
     *
     * @return 노출 중이면 true
     */
    public boolean isDisplayed() {
        return status.isDisplayed();
    }

    /**
     * 판매 가능 여부 확인
     *
     * @return 품절이 아니고 노출 중이면 true
     */
    public boolean isSellable() {
        return status.isSellable();
    }

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null && !id.isNew();
    }

    /**
     * 옵션 존재 여부 확인
     *
     * @return 옵션이 있으면 true
     */
    public boolean hasOption() {
        return optionType.hasOption();
    }

    /**
     * 2단 옵션 여부 확인
     *
     * @return 2단 옵션이면 true
     */
    public boolean isTwoLevelOption() {
        return optionType.isTwoLevel();
    }

    /**
     * 추가 금액 존재 여부 확인
     *
     * @return 추가 금액이 0보다 크면 true
     */
    public boolean hasAdditionalPrice() {
        return additionalPrice != null && additionalPrice.isPositive();
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 상품 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 상품그룹 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품그룹 ID Long 값
     */
    public Long getProductGroupIdValue() {
        return productGroupId != null ? productGroupId.value() : null;
    }

    /**
     * 옵션 타입명 반환 (Law of Demeter 준수)
     *
     * @return 옵션 타입 문자열
     */
    public String getOptionTypeValue() {
        return optionType != null ? optionType.name() : null;
    }

    /**
     * 추가 금액 값 반환 (Law of Demeter 준수)
     *
     * @return 추가 금액 BigDecimal 값
     */
    public java.math.BigDecimal getAdditionalPriceValue() {
        return additionalPrice != null ? additionalPrice.value() : null;
    }

    /**
     * 품절 여부 값 반환 (Law of Demeter 준수)
     *
     * @return 품절이면 true
     */
    public boolean getSoldOutValue() {
        return status != null && status.soldOut();
    }

    /**
     * 노출 여부 값 반환 (Law of Demeter 준수)
     *
     * @return 노출이면 true
     */
    public boolean getDisplayYnValue() {
        return status != null && status.displayYn();
    }

    // ========== 검증 메서드 ==========

    private static void validateProductGroupId(ProductGroupId productGroupId) {
        if (productGroupId == null) {
            throw new IllegalArgumentException("ProductGroupId is required");
        }
    }

    private static void validateOption1(String option1Name, String option1Value) {
        if (option1Name == null || option1Name.isBlank()) {
            throw new IllegalArgumentException("Option1 name is required for option product");
        }
        if (option1Value == null || option1Value.isBlank()) {
            throw new IllegalArgumentException("Option1 value is required for option product");
        }
    }

    private static void validateOption2(String option2Name, String option2Value) {
        if (option2Name == null || option2Name.isBlank()) {
            throw new IllegalArgumentException(
                    "Option2 name is required for two-level option product");
        }
        if (option2Value == null || option2Value.isBlank()) {
            throw new IllegalArgumentException(
                    "Option2 value is required for two-level option product");
        }
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ProductId getId() {
        return id;
    }

    public ProductGroupId getProductGroupId() {
        return productGroupId;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public String getOption1Name() {
        return option1Name;
    }

    public String getOption1Value() {
        return option1Value;
    }

    public String getOption2Name() {
        return option2Name;
    }

    public String getOption2Value() {
        return option2Value;
    }

    public Money getAdditionalPrice() {
        return additionalPrice;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
