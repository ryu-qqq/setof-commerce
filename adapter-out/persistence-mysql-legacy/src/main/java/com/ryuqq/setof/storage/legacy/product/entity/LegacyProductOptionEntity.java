package com.ryuqq.setof.storage.legacy.product.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyProductOptionEntity - 레거시 상품 옵션 엔티티.
 *
 * <p>레거시 DB의 product_option 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_option")
public class LegacyProductOptionEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "product_option_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "option_group_id")
    private Long optionGroupId;

    /** Added for productgroup composite support. option_group.option_group_name 조인 컬럼. */
    @Column(name = "option_group_name")
    private String optionGroupName;

    @Column(name = "option_detail_id")
    private Long optionDetailId;

    /** Added for productgroup composite support. option_detail.option_value 조인 컬럼. */
    @Column(name = "option_value")
    private String optionValue;

    @Column(name = "additional_price")
    private Long additionalPrice;

    /** Added for productgroup composite support. 옵션 삭제 여부 필터링. */
    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyProductOptionEntity() {}

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionGroupId() {
        return optionGroupId;
    }

    public String getOptionGroupName() {
        return optionGroupName;
    }

    public Long getOptionDetailId() {
        return optionDetailId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public Long getAdditionalPrice() {
        return additionalPrice;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
