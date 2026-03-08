package com.ryuqq.setof.storage.legacy.cart.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyCartEntity - 레거시 장바구니 엔티티.
 *
 * <p>레거시 DB의 cart 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "cart")
public class LegacyCartEntity extends LegacyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyCartEntity() {}

    private LegacyCartEntity(
            Long id,
            Long userId,
            Long productGroupId,
            Long productId,
            Integer quantity,
            Long sellerId,
            Yn deleteYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        super(insertDate, updateDate);
        this.id = id;
        this.userId = userId;
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.quantity = quantity;
        this.sellerId = sellerId;
        this.deleteYn = deleteYn;
    }

    public static LegacyCartEntity create(
            Long id,
            Long userId,
            Long productGroupId,
            Long productId,
            Integer quantity,
            Long sellerId,
            Yn deleteYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new LegacyCartEntity(
                id,
                userId,
                productGroupId,
                productId,
                quantity,
                sellerId,
                deleteYn,
                insertDate,
                updateDate);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    /**
     * 수량 변경 (JPA Dirty Checking용).
     *
     * <p>modifyCart / addToCart Upsert UPDATE 경로에서 사용됩니다. @Transactional 컨텍스트 내에서 호출하면 트랜잭션 커밋 시 자동
     * UPDATE가 발생합니다.
     *
     * @param newQuantity 새 수량
     */
    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        this.updateDate(LocalDateTime.now());
    }

    /**
     * 소프트 딜리트 상태 변경 (JPA Dirty Checking용).
     *
     * <p>deleteCarts 처리에서 사용됩니다. @Transactional 컨텍스트 내에서 호출하면 트랜잭션 커밋 시 자동 UPDATE가 발생합니다.
     *
     * @param yn 변경할 Yn 값 (Y: 삭제, N: 활성)
     */
    public void updateDeleteYn(Yn yn) {
        this.deleteYn = yn;
        this.updateDate(LocalDateTime.now());
    }
}
