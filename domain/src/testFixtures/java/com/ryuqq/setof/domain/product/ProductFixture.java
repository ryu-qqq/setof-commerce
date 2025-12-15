package com.ryuqq.setof.domain.product;

import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.Money;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Product TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Product 인스턴스 생성을 위한 팩토리 클래스
 */
public final class ProductFixture {

    private static final Instant DEFAULT_CREATED_AT = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant DEFAULT_UPDATED_AT = Instant.parse("2024-01-01T00:00:00Z");

    private ProductFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 상품 생성 (2단 옵션)
     *
     * @return Product 인스턴스
     */
    public static Product create() {
        return Product.reconstitute(
                ProductId.of(1L),
                ProductGroupId.of(1L),
                OptionType.TWO_LEVEL,
                "색상",
                "블랙",
                "사이즈",
                "M",
                Money.of(BigDecimal.valueOf(1000)),
                ProductStatus.defaultStatus(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * ID 지정하여 상품 생성
     *
     * @param id 상품 ID
     * @return Product 인스턴스
     */
    public static Product createWithId(Long id) {
        return Product.reconstitute(
                ProductId.of(id),
                ProductGroupId.of(1L),
                OptionType.TWO_LEVEL,
                "색상",
                "블랙",
                "사이즈",
                "M",
                Money.of(BigDecimal.valueOf(1000)),
                ProductStatus.defaultStatus(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 단품 상품 생성 (옵션 없음)
     *
     * @return Product 인스턴스
     */
    public static Product createSingle() {
        return Product.reconstitute(
                ProductId.of(2L),
                ProductGroupId.of(2L),
                OptionType.SINGLE,
                null,
                null,
                null,
                null,
                Money.zero(),
                ProductStatus.defaultStatus(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 1단 옵션 상품 생성
     *
     * @return Product 인스턴스
     */
    public static Product createOneLevel() {
        return Product.reconstitute(
                ProductId.of(3L),
                ProductGroupId.of(3L),
                OptionType.ONE_LEVEL,
                "색상",
                "화이트",
                null,
                null,
                Money.of(BigDecimal.valueOf(500)),
                ProductStatus.defaultStatus(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 2단 옵션 상품 생성
     *
     * @return Product 인스턴스
     */
    public static Product createTwoLevel() {
        return create();
    }

    /**
     * 품절 상품 생성
     *
     * @return Product 인스턴스 (품절)
     */
    public static Product createSoldOut() {
        return Product.reconstitute(
                ProductId.of(4L),
                ProductGroupId.of(1L),
                OptionType.TWO_LEVEL,
                "색상",
                "레드",
                "사이즈",
                "L",
                Money.of(BigDecimal.valueOf(1000)),
                ProductStatus.of(true, true),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 숨김 상품 생성
     *
     * @return Product 인스턴스 (숨김)
     */
    public static Product createHidden() {
        return Product.reconstitute(
                ProductId.of(5L),
                ProductGroupId.of(1L),
                OptionType.TWO_LEVEL,
                "색상",
                "블루",
                "사이즈",
                "S",
                Money.of(BigDecimal.valueOf(1000)),
                ProductStatus.of(false, false),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 삭제된 상품 생성
     *
     * @return Product 인스턴스 (삭제됨)
     */
    public static Product createDeleted() {
        Instant deletedAt = Instant.parse("2024-06-01T00:00:00Z");
        return Product.reconstitute(
                ProductId.of(6L),
                ProductGroupId.of(1L),
                OptionType.TWO_LEVEL,
                "색상",
                "그린",
                "사이즈",
                "XL",
                Money.of(BigDecimal.valueOf(1000)),
                ProductStatus.of(false, false),
                DEFAULT_CREATED_AT,
                deletedAt,
                deletedAt);
    }

    /**
     * 추가금액 없는 상품 생성
     *
     * @return Product 인스턴스
     */
    public static Product createWithoutAdditionalPrice() {
        return Product.reconstitute(
                ProductId.of(7L),
                ProductGroupId.of(1L),
                OptionType.TWO_LEVEL,
                "색상",
                "블랙",
                "사이즈",
                "M",
                Money.zero(),
                ProductStatus.defaultStatus(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 여러 상품 목록 생성 (2단 옵션 - 색상/사이즈 조합)
     *
     * @return Product 목록
     */
    public static List<Product> createList() {
        return List.of(
                createTwoLevelWithOptions("블랙", "S", 0),
                createTwoLevelWithOptions("블랙", "M", 0),
                createTwoLevelWithOptions("블랙", "L", 500),
                createTwoLevelWithOptions("화이트", "S", 0),
                createTwoLevelWithOptions("화이트", "M", 0),
                createTwoLevelWithOptions("화이트", "L", 500));
    }

    /**
     * 2단 옵션 상품 생성 (옵션값 지정)
     *
     * @param color 색상 값
     * @param size 사이즈 값
     * @param additionalPrice 추가금액
     * @return Product 인스턴스
     */
    public static Product createTwoLevelWithOptions(
            String color, String size, int additionalPrice) {
        return Product.reconstitute(
                ProductId.forNew(),
                ProductGroupId.of(1L),
                OptionType.TWO_LEVEL,
                "색상",
                color,
                "사이즈",
                size,
                Money.of(BigDecimal.valueOf(additionalPrice)),
                ProductStatus.defaultStatus(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 커스텀 상품 생성
     *
     * @param id 상품 ID
     * @param productGroupId 상품그룹 ID
     * @param optionType 옵션 타입
     * @param option1Name 옵션1 이름
     * @param option1Value 옵션1 값
     * @param option2Name 옵션2 이름
     * @param option2Value 옵션2 값
     * @param additionalPrice 추가금액
     * @param soldOut 품절 여부
     * @param displayYn 노출 여부
     * @return Product 인스턴스
     */
    public static Product createCustom(
            Long id,
            Long productGroupId,
            OptionType optionType,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            BigDecimal additionalPrice,
            boolean soldOut,
            boolean displayYn) {
        return Product.reconstitute(
                ProductId.of(id),
                ProductGroupId.of(productGroupId),
                optionType,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                Money.of(additionalPrice),
                ProductStatus.of(soldOut, displayYn),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 신규 생성용 단품 Product 생성 (ID 없음)
     *
     * @param productGroupId 상품그룹 ID
     * @return Product 인스턴스 (ID null)
     */
    public static Product createNewSingle(ProductGroupId productGroupId) {
        return Product.createSingle(productGroupId, Money.zero(), DEFAULT_CREATED_AT);
    }

    /**
     * 신규 생성용 1단 옵션 Product 생성 (ID 없음)
     *
     * @param productGroupId 상품그룹 ID
     * @param optionName 옵션명
     * @param optionValue 옵션값
     * @return Product 인스턴스 (ID null)
     */
    public static Product createNewOneLevel(
            ProductGroupId productGroupId, String optionName, String optionValue) {
        return Product.createOneLevel(
                productGroupId, optionName, optionValue, Money.zero(), DEFAULT_CREATED_AT);
    }

    /**
     * 신규 생성용 2단 옵션 Product 생성 (ID 없음)
     *
     * @param productGroupId 상품그룹 ID
     * @param option1Name 옵션1명
     * @param option1Value 옵션1값
     * @param option2Name 옵션2명
     * @param option2Value 옵션2값
     * @return Product 인스턴스 (ID null)
     */
    public static Product createNewTwoLevel(
            ProductGroupId productGroupId,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value) {
        return Product.createTwoLevel(
                productGroupId,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                Money.of(BigDecimal.valueOf(1000)),
                DEFAULT_CREATED_AT);
    }
}
