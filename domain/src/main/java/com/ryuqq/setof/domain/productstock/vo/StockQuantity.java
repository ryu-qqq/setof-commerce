package com.ryuqq.setof.domain.productstock.vo;

import com.ryuqq.setof.domain.productstock.exception.NotEnoughStockException;
import com.ryuqq.setof.domain.productstock.exception.StockOverflowException;

/**
 * 재고 수량 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>음수 수량 허용 안함
 *   <li>최대 수량: Integer.MAX_VALUE
 * </ul>
 *
 * @param value 재고 수량 값
 */
public record StockQuantity(int value) {

    /** 최대 재고 수량 */
    public static final int MAX_QUANTITY = Integer.MAX_VALUE;

    /** Compact Constructor - 검증 로직 */
    public StockQuantity {
        validate(value);
    }

    /**
     * Static Factory Method - 수량 생성
     *
     * @param value int 값
     * @return StockQuantity 인스턴스
     * @throws NotEnoughStockException value가 음수인 경우
     */
    public static StockQuantity of(int value) {
        return new StockQuantity(value);
    }

    /**
     * Static Factory Method - 0 수량 생성
     *
     * @return 0인 StockQuantity 인스턴스
     */
    public static StockQuantity zero() {
        return new StockQuantity(0);
    }

    /**
     * 재고 여부 확인
     *
     * @return 재고가 0보다 크면 true
     */
    public boolean hasStock() {
        return value > 0;
    }

    /**
     * 재고 없음 여부 확인
     *
     * @return 재고가 0이면 true
     */
    public boolean isEmpty() {
        return value == 0;
    }

    /**
     * 충분한 재고 확인
     *
     * @param quantity 확인할 수량
     * @return 현재 재고가 요청 수량 이상이면 true
     */
    public boolean isEnough(int quantity) {
        return value >= quantity;
    }

    /**
     * 재고 차감
     *
     * @param quantity 차감할 수량
     * @param productId 상품 ID (예외 메시지용)
     * @return 차감된 새로운 StockQuantity
     * @throws NotEnoughStockException 재고 부족 시
     */
    public StockQuantity deduct(int quantity, Long productId) {
        if (quantity < 0) {
            throw new NotEnoughStockException(productId, value, quantity, "차감 수량은 0 이상이어야 합니다");
        }
        if (!isEnough(quantity)) {
            throw new NotEnoughStockException(productId, value, quantity, "재고가 부족합니다");
        }
        return new StockQuantity(value - quantity);
    }

    /**
     * 재고 복원
     *
     * @param quantity 복원할 수량
     * @param productId 상품 ID (예외 메시지용)
     * @return 복원된 새로운 StockQuantity
     * @throws StockOverflowException 최대 재고 초과 시
     */
    public StockQuantity restore(int quantity, Long productId) {
        if (quantity < 0) {
            throw new StockOverflowException(productId, value, quantity, "복원 수량은 0 이상이어야 합니다");
        }
        long newValue = (long) value + quantity;
        if (newValue > MAX_QUANTITY) {
            throw new StockOverflowException(productId, value, quantity, "최대 재고 수량을 초과합니다");
        }
        return new StockQuantity((int) newValue);
    }

    /**
     * 재고 설정
     *
     * @param quantity 설정할 수량
     * @return 새로운 StockQuantity
     */
    public StockQuantity set(int quantity) {
        return new StockQuantity(quantity);
    }

    private static void validate(int value) {
        if (value < 0) {
            throw new NotEnoughStockException(null, 0, value, "재고 수량은 0 이상이어야 합니다");
        }
    }
}
