package com.ryuqq.setof.storage.legacy.brand.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyBrandEntity - 레거시 브랜드 엔티티.
 *
 * <p>레거시 DB의 brand 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "brand")
public class LegacyBrandEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "brand_id")
    private Long id;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "brand_icon_image_url")
    private String brandIconImageUrl;

    @Column(name = "display_english_name")
    private String displayEnglishName;

    @Column(name = "display_korean_name")
    private String displayKoreanName;

    @Column(name = "main_display_type")
    @Enumerated(EnumType.STRING)
    private MainDisplayNameType mainDisplayType;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    protected LegacyBrandEntity() {}

    public Long getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBrandIconImageUrl() {
        return brandIconImageUrl;
    }

    public String getDisplayEnglishName() {
        return displayEnglishName;
    }

    public String getDisplayKoreanName() {
        return displayKoreanName;
    }

    public MainDisplayNameType getMainDisplayType() {
        return mainDisplayType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    /**
     * 표시 여부 확인.
     *
     * @return 표시 여부 (displayYn == Y)
     */
    public boolean isDisplayed() {
        return displayYn != null && displayYn.toBoolean();
    }

    /** MainDisplayNameType - 메인 표시명 타입. */
    public enum MainDisplayNameType {
        KR,
        US
    }
}
