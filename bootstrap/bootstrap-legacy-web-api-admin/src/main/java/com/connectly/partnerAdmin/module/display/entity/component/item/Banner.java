package com.connectly.partnerAdmin.module.display.entity.component.item;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBanner;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "BANNER")
@Entity
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BANNER_ID")
    private long id;
    @Column(name = "TITLE")
    private String title;

    @Column(name = "BANNER_TYPE")
    @Enumerated(EnumType.STRING)
    private BannerType bannerType;
    @Embedded
    private DisplayPeriod displayPeriod;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;


    public void update(CreateBanner createBanner){
        title = createBanner.getTitle();
        bannerType = createBanner.getBannerType();
        displayPeriod = createBanner.getDisplayPeriod();
        displayYn = createBanner.getDisplayYn();
    }

    public void updateDisplayYn(Yn displayYn){
        this.displayYn = displayYn;
    }

}
