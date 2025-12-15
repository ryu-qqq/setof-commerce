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
 * ProductJpaEntity - Product (SKU) JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 products 테이블과 매핑됩니다.
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
 *   <li>productGroupId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "products")
public class ProductJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품그룹 ID (Long FK) */
    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    /** 옵션 타입 */
    @Column(name = "option_type", nullable = false, length = 20)
    private String optionType;

    /** 옵션1 이름 */
    @Column(name = "option1_name", length = 50)
    private String option1Name;

    /** 옵션1 값 */
    @Column(name = "option1_value", length = 100)
    private String option1Value;

    /** 옵션2 이름 */
    @Column(name = "option2_name", length = 50)
    private String option2Name;

    /** 옵션2 값 */
    @Column(name = "option2_value", length = 100)
    private String option2Value;

    /** 추가 금액 */
    @Column(name = "additional_price", precision = 15, scale = 2)
    private BigDecimal additionalPrice;

    /** 품절 여부 */
    @Column(name = "sold_out", nullable = false)
    private Boolean soldOut;

    /** 노출 여부 */
    @Column(name = "display_yn", nullable = false)
    private Boolean displayYn;

    /** 삭제 일시 (Soft Delete) */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    /** JPA 기본 생성자 (protected) */
    protected ProductJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductJpaEntity(
            Long id,
            Long productGroupId,
            String optionType,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            BigDecimal additionalPrice,
            Boolean soldOut,
            Boolean displayYn,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.optionType = optionType;
        this.option1Name = option1Name;
        this.option1Value = option1Value;
        this.option2Name = option2Name;
        this.option2Value = option2Value;
        this.additionalPrice = additionalPrice;
        this.soldOut = soldOut;
        this.displayYn = displayYn;
        this.deletedAt = deletedAt;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductJpaEntity of(
            Long id,
            Long productGroupId,
            String optionType,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            BigDecimal additionalPrice,
            Boolean soldOut,
            Boolean displayYn,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ProductJpaEntity(
                id,
                productGroupId,
                optionType,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                additionalPrice,
                soldOut,
                displayYn,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getOptionType() {
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

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public Boolean getSoldOut() {
        return soldOut;
    }

    public Boolean getDisplayYn() {
        return displayYn;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
