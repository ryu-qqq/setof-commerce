package com.ryuqq.setof.domain.cart.vo;

import com.ryuqq.setof.domain.cart.exception.InvalidCartItemException;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * CartItem Value Object
 *
 * <p>장바구니에 담긴 상품 항목입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long FK 전략 - JPA 관계 어노테이션 사용 안함
 * </ul>
 *
 * <p>소프트 딜리트 지원:
 *
 * <ul>
 *   <li>결제 시작 시 deletedAt 설정 (소프트 딜리트)
 *   <li>결제 실패 시 deletedAt null로 복원
 *   <li>결제 성공 시 삭제 상태 유지
 * </ul>
 *
 * @param id 장바구니 아이템 ID (신규 생성 시 null 가능)
 * @param productStockId 상품 재고 ID (SKU)
 * @param productId 상품 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 담은 시점 가격
 * @param selected 체크 여부
 * @param addedAt 담은 시각
 * @param deletedAt 삭제 시각 (소프트 딜리트, null이면 활성 상태)
 */
public record CartItem(
        CartItemId id,
        Long productStockId,
        Long productId,
        Long productGroupId,
        Long sellerId,
        int quantity,
        BigDecimal unitPrice,
        boolean selected,
        Instant addedAt,
        Instant deletedAt) {

    /** 상품 최대 수량 */
    public static final int MAX_QUANTITY = 99;

    /** Compact Constructor - 검증 로직 */
    public CartItem {
        validatePositive(productStockId, "productStockId");
        validatePositive(productId, "productId");
        validatePositive(productGroupId, "productGroupId");
        validatePositive(sellerId, "sellerId");
        validateQuantity(quantity);
        validateNotNull(unitPrice, "unitPrice");
        validateNotNull(addedAt, "addedAt");
    }

    /**
     * Static Factory Method - 신규 아이템 생성
     *
     * @param productStockId 상품 재고 ID
     * @param productId 상품 ID
     * @param productGroupId 상품 그룹 ID
     * @param sellerId 판매자 ID
     * @param quantity 수량
     * @param unitPrice 담은 시점 가격
     * @param now 현재 시각
     * @return CartItem 인스턴스
     */
    public static CartItem forNew(
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            Instant now) {
        return new CartItem(
                null, // 신규 생성 시 ID 없음
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                true, // 기본 선택 상태
                now,
                null); // 활성 상태
    }

    /**
     * Static Factory Method - 영속화된 데이터로부터 복원
     *
     * @param id 장바구니 아이템 ID
     * @param productStockId 상품 재고 ID
     * @param productId 상품 ID
     * @param productGroupId 상품 그룹 ID
     * @param sellerId 판매자 ID
     * @param quantity 수량
     * @param unitPrice 담은 시점 가격
     * @param selected 체크 여부
     * @param addedAt 담은 시각
     * @param deletedAt 삭제 시각 (소프트 딜리트)
     * @return CartItem 인스턴스
     */
    public static CartItem restore(
            CartItemId id,
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            boolean selected,
            Instant addedAt,
            Instant deletedAt) {
        return new CartItem(
                id,
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                selected,
                addedAt,
                deletedAt);
    }

    // ===== 도메인 로직 =====

    /**
     * 수량 변경된 새 인스턴스 반환
     *
     * @param newQuantity 새 수량
     * @return 수량이 변경된 CartItem
     */
    public CartItem withQuantity(int newQuantity) {
        validateQuantity(newQuantity);
        return new CartItem(
                id,
                productStockId,
                productId,
                productGroupId,
                sellerId,
                newQuantity,
                unitPrice,
                selected,
                addedAt,
                deletedAt);
    }

    /**
     * 선택 상태 변경된 새 인스턴스 반환
     *
     * @param newSelected 새 선택 상태
     * @return 선택 상태가 변경된 CartItem
     */
    public CartItem withSelected(boolean newSelected) {
        return new CartItem(
                id,
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                newSelected,
                addedAt,
                deletedAt);
    }

    /**
     * ID가 할당된 새 인스턴스 반환 (영속화 후)
     *
     * @param newId 할당된 ID
     * @return ID가 할당된 CartItem
     */
    public CartItem withId(CartItemId newId) {
        return new CartItem(
                newId,
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                selected,
                addedAt,
                deletedAt);
    }

    /**
     * 소프트 딜리트 처리된 새 인스턴스 반환
     *
     * @param now 삭제 시각
     * @return 삭제 상태가 설정된 CartItem
     */
    public CartItem withDeleted(Instant now) {
        if (now == null) {
            throw new InvalidCartItemException("삭제 시각은 필수입니다");
        }
        return new CartItem(
                id,
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                selected,
                addedAt,
                now);
    }

    /**
     * 소프트 딜리트 복원된 새 인스턴스 반환
     *
     * @return 활성 상태로 복원된 CartItem
     */
    public CartItem withRestored() {
        return new CartItem(
                id,
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                selected,
                addedAt,
                null);
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
     * 활성 상태 여부 확인
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return deletedAt == null;
    }

    /**
     * 동일 상품과 수량 병합
     *
     * @param other 병합할 CartItem
     * @return 수량이 합산된 CartItem
     * @throws InvalidCartItemException 다른 상품인 경우
     */
    public CartItem mergeWith(CartItem other) {
        if (!this.productStockId.equals(other.productStockId)) {
            throw new InvalidCartItemException("동일 상품만 병합할 수 있습니다");
        }
        int mergedQuantity = this.quantity + other.quantity;
        validateQuantity(mergedQuantity);
        return this.withQuantity(mergedQuantity);
    }

    /**
     * 총 금액 계산
     *
     * @return 수량 * 단가
     */
    public BigDecimal totalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * 동일 상품 여부 확인
     *
     * @param otherProductStockId 비교할 상품 재고 ID
     * @return 동일 상품이면 true
     */
    public boolean isSameProduct(Long otherProductStockId) {
        return this.productStockId.equals(otherProductStockId);
    }

    /**
     * 동일 판매자 여부 확인
     *
     * @param otherSellerId 비교할 판매자 ID
     * @return 동일 판매자이면 true
     */
    public boolean isSameSeller(Long otherSellerId) {
        return this.sellerId.equals(otherSellerId);
    }

    /**
     * 신규 아이템 여부 (ID 미할당)
     *
     * @return 신규이면 true
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * 아이템 ID 일치 여부 확인 (Law of Demeter 준수)
     *
     * @param cartItemId 비교할 아이템 ID
     * @return ID가 일치하면 true
     */
    public boolean hasId(CartItemId cartItemId) {
        if (this.id == null || cartItemId == null) {
            return false;
        }
        return this.id.equals(cartItemId);
    }

    // ===== Private Helper =====

    private static void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new InvalidCartItemException(fieldName + "은(는) 양수여야 합니다");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidCartItemException("수량은 1 이상이어야 합니다");
        }
        if (quantity > MAX_QUANTITY) {
            throw new InvalidCartItemException(String.format("수량은 %d개를 초과할 수 없습니다", MAX_QUANTITY));
        }
    }

    private static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidCartItemException(fieldName + "은(는) 필수입니다");
        }
    }
}
