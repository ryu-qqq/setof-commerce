package com.setof.connectly.module.display.entity.embedded;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Embedded private DisplayPeriod displayPeriod;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @QueryProjection
    public GnbDetails(
            String title,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            Yn displayYn) {
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
