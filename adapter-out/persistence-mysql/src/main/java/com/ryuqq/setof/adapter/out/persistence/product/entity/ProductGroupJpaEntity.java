package com.ryuqq.setof.adapter.out.persistence.product.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * ProductGroupJpaEntity - ProductGroup JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_groups 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>sellerId, categoryId, brandId: Long 타입으로 FK 관리
 *   <li>shippingPolicyId, refundPolicyId: Long 타입 (nullable)
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "product_groups")
public class ProductGroupJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 셀러 ID (Long FK) */
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    /** 카테고리 ID (Long FK) */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    /** 브랜드 ID (Long FK) */
    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    /** 상품그룹명 */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /** 옵션 타입 */
    @Column(name = "option_type", nullable = false, length = 20)
    private String optionType;

    /** 정가 */
    @Column(name = "regular_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal regularPrice;

    /** 판매가 */
    @Column(name = "current_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentPrice;

    /** 상태 */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 배송 정책 ID (Long FK, nullable) */
    @Column(name = "shipping_policy_id")
    private Long shippingPolicyId;

    /** 환불 정책 ID (Long FK, nullable) */
    @Column(name = "refund_policy_id")
    private Long refundPolicyId;

    /** 삭제 일시 (Soft Delete) */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    /** JPA 기본 생성자 (protected) */
    protected ProductGroupJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductGroupJpaEntity(
            Long id,
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String optionType,
            BigDecimal regularPrice,
            BigDecimal currentPrice,
            String status,
            Long shippingPolicyId,
            Long refundPolicyId,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.name = name;
        this.optionType = optionType;
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.status = status;
        this.shippingPolicyId = shippingPolicyId;
        this.refundPolicyId = refundPolicyId;
        this.deletedAt = deletedAt;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductGroupJpaEntity of(
            Long id,
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String optionType,
            BigDecimal regularPrice,
            BigDecimal currentPrice,
            String status,
            Long shippingPolicyId,
            Long refundPolicyId,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ProductGroupJpaEntity(
                id,
                sellerId,
                categoryId,
                brandId,
                name,
                optionType,
                regularPrice,
                currentPrice,
                status,
                shippingPolicyId,
                refundPolicyId,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getName() {
        return name;
    }

    public String getOptionType() {
        return optionType;
    }

    public BigDecimal getRegularPrice() {
        return regularPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public String getStatus() {
        return status;
    }

    public Long getShippingPolicyId() {
        return shippingPolicyId;
    }

    public Long getRefundPolicyId() {
        return refundPolicyId;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
