package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.product.exception.InvalidPriceException;
import java.math.BigDecimal;

/**
 * 상품 가격 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>비즈니스 규칙: regularPrice >= currentPrice
 * </ul>
 *
 * @param regularPrice 정가
 * @param currentPrice 판매가
 */
public record Price(BigDecimal regularPrice, BigDecimal currentPrice) {

    /** Compact Constructor - 검증 로직 */
    public Price {
        validate(regularPrice, currentPrice);
    }

    /**
     * Static Factory Method - 가격 생성
     *
     * @param regularPrice 정가
     * @param currentPrice 판매가
     * @return Price 인스턴스
     * @throws InvalidPriceException 가격이 유효하지 않은 경우
     */
    public static Price of(BigDecimal regularPrice, BigDecimal currentPrice) {
        return new Price(regularPrice, currentPrice);
    }

    /**
     * Static Factory Method - 동일 가격 (정가 = 판매가)
     *
     * @param price 가격
     * @return Price 인스턴스
     */
    public static Price samePrice(BigDecimal price) {
        return new Price(price, price);
    }

    /**
     * 할인 여부 확인
     *
     * @return 할인 중이면 true (정가 > 판매가)
     */
    public boolean isDiscounted() {
        return regularPrice.compareTo(currentPrice) > 0;
    }

    /**
     * 할인율 계산
     *
     * @return 할인율 (0 ~ 100 사이 정수)
     */
    public int discountPercentage() {
        if (!isDiscounted()) {
            return 0;
        }
        BigDecimal discount = regularPrice.subtract(currentPrice);
        return discount.multiply(BigDecimal.valueOf(100))
                .divide(regularPrice, 0, java.math.RoundingMode.HALF_UP)
                .intValue();
    }

    /**
     * 할인 금액 계산
     *
     * @return 할인 금액 (정가 - 판매가)
     */
    public BigDecimal discountAmount() {
        return regularPrice.subtract(currentPrice);
    }

    private static void validate(BigDecimal regularPrice, BigDecimal currentPrice) {
        if (regularPrice == null) {
            throw new InvalidPriceException(BigDecimal.ZERO, BigDecimal.ZERO, "정가는 필수입니다");
        }
        if (currentPrice == null) {
            throw new InvalidPriceException(regularPrice, BigDecimal.ZERO, "판매가는 필수입니다");
        }
        if (regularPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException(regularPrice, currentPrice, "정가는 0 이상이어야 합니다");
        }
        if (currentPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException(regularPrice, currentPrice, "판매가는 0 이상이어야 합니다");
        }
        if (regularPrice.compareTo(currentPrice) < 0) {
            throw new InvalidPriceException(regularPrice, currentPrice, "정가는 판매가보다 크거나 같아야 합니다");
        }
    }
}
