package com.connectly.partnerAdmin.module.brand.core;

import com.connectly.partnerAdmin.module.brand.enums.MainDisplayNameType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtendedBrandContext extends BaseBrandContext {

    private MainDisplayNameType mainDisplayType;
    private String displayEnglishName;
    private String displayKoreanName;

    @QueryProjection
    public ExtendedBrandContext(long brandId, String brandName, MainDisplayNameType mainDisplayType, String displayEnglishName, String displayKoreanName) {
        super(brandId, brandName);
        this.mainDisplayType = mainDisplayType;
        this.displayEnglishName = displayEnglishName;
        this.displayKoreanName = displayKoreanName;
    }
}
