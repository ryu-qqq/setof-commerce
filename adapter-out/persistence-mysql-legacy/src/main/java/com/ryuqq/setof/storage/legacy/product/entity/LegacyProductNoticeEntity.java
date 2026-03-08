package com.ryuqq.setof.storage.legacy.product.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "notice_detail", columnDefinition = "TEXT")
    private String noticeDetail;

    @Column(name = "material_info")
    private String materialInfo;

    @Column(name = "wash_info")
    private String washInfo;

    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Column(name = "manufacturer")
    private String manufacturer;

    protected LegacyProductNoticeEntity() {}

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getNoticeDetail() {
        return noticeDetail;
    }

    public String getMaterialInfo() {
        return materialInfo;
    }

    public String getWashInfo() {
        return washInfo;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
