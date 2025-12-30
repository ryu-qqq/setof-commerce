package com.connectly.partnerAdmin.module.display.entity.embedded;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import jakarta.persistence.*;



@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class GnbDetails {
    @Column(name = "TITLE")
    private String title;
    @Column(name = "LINK_URL")
    private String linkUrl;
    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;
    @Embedded
    private DisplayPeriod displayPeriod;
    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @QueryProjection
    public GnbDetails(String title, String linkUrl, int displayOrder, DisplayPeriod displayPeriod, Yn displayYn) {
        this.title = title;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayPeriod = displayPeriod;
        this.displayYn = displayYn;
    }

    public void setDisplayYn(Yn displayYn) {
        this.displayYn = displayYn;
    }
}
