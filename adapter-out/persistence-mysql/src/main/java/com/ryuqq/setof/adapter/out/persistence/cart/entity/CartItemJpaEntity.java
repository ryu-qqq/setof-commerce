package com.ryuqq.setof.adapter.out.persistence.cart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * CartItemJpaEntity - CartItem JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cart_items 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등)
 *   <li>부모 엔티티 참조는 Long FK 필드 사용 (cartId)
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cart_items")
public class CartItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Cart ID (Long FK) */
    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "product_stock_id", nullable = false)
    private Long productStockId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "selected", nullable = false)
    private boolean selected;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt;

    /** 소프트 딜리트 시각 (null이면 활성 상태) */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected CartItemJpaEntity() {
        // JPA 기본 생성자
    }

    private CartItemJpaEntity(
            Long id,
            Long cartId,
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            boolean selected,
            Instant addedAt,
            Instant deletedAt) {
        this.id = id;
        this.cartId = cartId;
        this.productStockId = productStockId;
        this.productId = productId;
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.selected = selected;
        this.addedAt = addedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 Entity 생성 (ID 미할당)
     *
     * @param cartId 장바구니 ID
     * @param productStockId 상품 재고 ID
     * @param productId 상품 ID
     * @param productGroupId 상품 그룹 ID
     * @param sellerId 판매자 ID
     * @param quantity 수량
     * @param unitPrice 단가
     * @param selected 선택 상태
     * @param addedAt 담은 시각
     * @param deletedAt 삭제 시각 (소프트 딜리트)
     * @return CartItemJpaEntity
     */
    public static CartItemJpaEntity forNew(
            Long cartId,
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            boolean selected,
            Instant addedAt,
            Instant deletedAt) {
        return new CartItemJpaEntity(
                null,
                cartId,
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

    /** 영속화된 데이터로부터 복원 */
    public static CartItemJpaEntity of(
            Long id,
            Long cartId,
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            boolean selected,
            Instant addedAt,
            Instant deletedAt) {
        return new CartItemJpaEntity(
                id,
                cartId,
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

    public Long getId() {
        return id;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getProductStockId() {
        return productStockId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public boolean isSelected() {
        return selected;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    /** 삭제 여부 확인 */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /** 활성 상태 여부 확인 */
    public boolean isActive() {
        return deletedAt == null;
    }
}
