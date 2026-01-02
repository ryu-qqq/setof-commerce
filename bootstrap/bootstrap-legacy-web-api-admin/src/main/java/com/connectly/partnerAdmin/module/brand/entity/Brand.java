package com.connectly.partnerAdmin.module.brand.entity;

import com.connectly.partnerAdmin.module.brand.enums.MainDisplayNameType;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "brand")
@Entity
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private long id;

    @Column(name = "BRAND_NAME", nullable = false, length = 50)
    private String brandName;

    @Column(name = "BRAND_ICON_IMAGE_URL", nullable = false, length = 255)
    private String brandIconImageUrl;

    @Column(name = "DISPLAY_ENGLISH_NAME", nullable = false, length = 50)
    private String displayEnglishName;

    @Column(name = "DISPLAY_KOREAN_NAME", nullable = false, length = 50)
    private String displayKoreanName;

    @Enumerated(EnumType.STRING)
    @Column(name = "MAIN_DISPLAY_TYPE",  nullable = false, length = 50)
    private MainDisplayNameType mainDisplayType;

    @Column(name = "DISPLAY_ORDER",  nullable = false)
    private int displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISPLAY_YN", nullable = false, length = 1)
    private Yn displayYn;

}
