package com.ryuqq.setof.storage.legacy.productgroup.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyProductNoticeEntity - 레거시 상품 공지 엔티티.
 *
 * <p>레거시 DB의 product_notice 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_notice")
public class LegacyProductNoticeEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "MATERIAL")
    private String material;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "MAKER")
    private String maker;

    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "WASHING_METHOD")
    private String washingMethod;

    @Column(name = "YEAR_MONTH_DAY")
    private String yearMonthDay;

    @Column(name = "ASSURANCE_STANDARD")
    private String assuranceStandard;

    @Column(name = "AS_PHONE")
    private String asPhone;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyProductNoticeEntity() {}

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getMaterial() {
        return material;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getMaker() {
        return maker;
    }

    public String getOrigin() {
        return origin;
    }

    public String getWashingMethod() {
        return washingMethod;
    }

    public String getYearMonthDay() {
        return yearMonthDay;
    }

    public String getAssuranceStandard() {
        return assuranceStandard;
    }

    public String getAsPhone() {
        return asPhone;
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
