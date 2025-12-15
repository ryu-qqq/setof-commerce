package com.ryuqq.setof.adapter.out.persistence.productstock.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;

/**
 * ProductStockJpaEntity - ProductStock JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_stocks 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 *   <li>재고는 Soft Delete 미적용
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>productId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "product_stocks")
public class ProductStockJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품 ID (Long FK, UNIQUE) */
    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    /** 재고 수량 */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 낙관적 락 버전
     *
     * <p>동시성 제어를 위한 버전 필드입니다. JPA가 자동으로 관리합니다.
     *
     * <p>업데이트 시 버전 불일치 시 OptimisticLockException 발생
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /** JPA 기본 생성자 (protected) */
    protected ProductStockJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductStockJpaEntity(
            Long id,
            Long productId,
            Integer quantity,
            Long version,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.version = version;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductStockJpaEntity of(
            Long id,
            Long productId,
            Integer quantity,
            Long version,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductStockJpaEntity(id, productId, quantity, version, createdAt, updatedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getVersion() {
        return version;
    }
}
