package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidCostShareException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 비용 분담 비율 Value Object
 *
 * <p>할인 비용을 플랫폼과 셀러가 분담하는 비율을 표현합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>BigDecimal 사용 - 정확한 비율 계산
 * </ul>
 *
 * @param platformRatio 플랫폼 분담 비율 (0.00 ~ 100.00)
 * @param sellerRatio 셀러 분담 비율 (0.00 ~ 100.00)
 */
public record CostShare(BigDecimal platformRatio, BigDecimal sellerRatio) {

    private static final BigDecimal HUNDRED = new BigDecimal("100.00");

    /** Compact Constructor - 검증 로직 */
    public CostShare {
        validate(platformRatio, sellerRatio);
        platformRatio = platformRatio.setScale(2, RoundingMode.HALF_UP);
        sellerRatio = sellerRatio.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Static Factory Method
     *
     * @param platformRatio 플랫폼 분담 비율
     * @param sellerRatio 셀러 분담 비율
     * @return CostShare 인스턴스
     */
    public static CostShare of(BigDecimal platformRatio, BigDecimal sellerRatio) {
        return new CostShare(platformRatio, sellerRatio);
    }

    /**
     * Static Factory Method - 플랫폼 전액 부담
     *
     * @return 플랫폼 100% 부담 CostShare 인스턴스
     */
    public static CostShare platformOnly() {
        return new CostShare(HUNDRED, BigDecimal.ZERO);
    }

    /**
     * Static Factory Method - 셀러 전액 부담
     *
     * @return 셀러 100% 부담 CostShare 인스턴스
     */
    public static CostShare sellerOnly() {
        return new CostShare(BigDecimal.ZERO, HUNDRED);
    }

    /**
     * Static Factory Method - 균등 분담 (50:50)
     *
     * @return 50:50 분담 CostShare 인스턴스
     */
    public static CostShare equalShare() {
        BigDecimal half = new BigDecimal("50.00");
        return new CostShare(half, half);
    }

    private static void validate(BigDecimal platformRatio, BigDecimal sellerRatio) {
        if (platformRatio == null) {
            throw new InvalidCostShareException(null, sellerRatio, "플랫폼 분담 비율은 필수입니다.");
        }
        if (sellerRatio == null) {
            throw new InvalidCostShareException(platformRatio, null, "셀러 분담 비율은 필수입니다.");
        }
        if (platformRatio.compareTo(BigDecimal.ZERO) < 0 || platformRatio.compareTo(HUNDRED) > 0) {
            throw new InvalidCostShareException(
                    platformRatio, sellerRatio, "플랫폼 분담 비율은 0% ~ 100% 사이여야 합니다.");
        }
        if (sellerRatio.compareTo(BigDecimal.ZERO) < 0 || sellerRatio.compareTo(HUNDRED) > 0) {
            throw new InvalidCostShareException(
                    platformRatio, sellerRatio, "셀러 분담 비율은 0% ~ 100% 사이여야 합니다.");
        }
        BigDecimal total = platformRatio.add(sellerRatio);
        if (total.compareTo(HUNDRED) != 0) {
            throw new InvalidCostShareException(
                    platformRatio, sellerRatio, "플랫폼과 셀러의 분담 비율 합은 100%여야 합니다.");
        }
    }

    /**
     * 할인 금액에서 플랫폼 부담액 계산
     *
     * @param discountAmount 총 할인 금액
     * @return 플랫폼 부담 금액
     */
    public long calculatePlatformCost(long discountAmount) {
        return BigDecimal.valueOf(discountAmount)
                .multiply(platformRatio)
                .divide(HUNDRED, 0, RoundingMode.DOWN)
                .longValue();
    }

    /**
     * 할인 금액에서 셀러 부담액 계산
     *
     * @param discountAmount 총 할인 금액
     * @return 셀러 부담 금액
     */
    public long calculateSellerCost(long discountAmount) {
        long platformCost = calculatePlatformCost(discountAmount);
        return discountAmount - platformCost;
    }

    /**
     * 플랫폼이 전액 부담하는지 확인
     *
     * @return 플랫폼 전액 부담이면 true
     */
    public boolean isPlatformOnly() {
        return platformRatio.compareTo(HUNDRED) == 0;
    }

    /**
     * 셀러가 전액 부담하는지 확인
     *
     * @return 셀러 전액 부담이면 true
     */
    public boolean isSellerOnly() {
        return sellerRatio.compareTo(HUNDRED) == 0;
    }
}
