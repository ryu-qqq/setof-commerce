package com.ryuqq.setof.storage.legacy.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyProductGroupEntity - 레거시 상품그룹 엔티티.
 *
 * <p>레거시 DB의 product_group 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_group")
public class LegacyProductGroupEntity {

    @Id
    @Column(name = "product_group_id")
    private Long id;

    @Column(name = "product_group_name")
    private String productGroupName;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "regular_price")
    private Integer regularPrice;

    @Column(name = "current_price")
    private Integer currentPrice;

    @Column(name = "sale_price")
    private Integer salePrice;

    @Column(name = "option_type")
    @Enumerated(EnumType.STRING)
    private OptionType optionType;

    @Column(name = "management_type")
    @Enumerated(EnumType.STRING)
    private ManagementType managementType;

    @Column(name = "sold_out_yn")
    @Enumerated(EnumType.STRING)
    private Yn soldOutYn;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected LegacyProductGroupEntity() {}

    public Long getId() {
        return id;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Integer getRegularPrice() {
        return regularPrice;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public ManagementType getManagementType() {
        return managementType;
    }

    public Yn getSoldOutYn() {
        return soldOutYn;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /** OptionType - 옵션 타입 Enum. */
    public enum OptionType {
        SINGLE,
        COMBINATION
    }

    /** ManagementType - 관리 타입 Enum. */
    public enum ManagementType {
        SELF,
        CONSIGNMENT
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
