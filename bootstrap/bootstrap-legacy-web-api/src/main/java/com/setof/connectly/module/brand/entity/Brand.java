package com.setof.connectly.module.brand.entity;

import com.setof.connectly.module.brand.enums.MainDisplayNameType;
import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "brand")
@Entity
public class Brand extends BaseEntity {

    @Id
    @Column(name = "brand_id")
    private long id;

    private String brandName;
    private String brandIconImageUrl;
    private String displayEnglishName;
    private String displayKoreanName;

    @Enumerated(EnumType.STRING)
    private MainDisplayNameType mainDisplayType;

    private int displayOrder;

    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    protected Brand() {}

    public Brand(
            long id,
            String brandName,
            String brandIconImageUrl,
            String displayEnglishName,
            String displayKoreanName,
            MainDisplayNameType mainDisplayType,
            int displayOrder,
            Yn displayYn) {
        this.id = id;
        this.brandName = brandName;
        this.brandIconImageUrl = brandIconImageUrl;
        this.displayEnglishName = displayEnglishName;
        this.displayKoreanName = displayKoreanName;
        this.mainDisplayType = mainDisplayType;
        this.displayOrder = displayOrder;
        this.displayYn = displayYn;
    }

    public long getId() {
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
}
