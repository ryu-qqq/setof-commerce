package com.setof.connectly.module.event.entity.embedded;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class EventDetail {

    @Column(name = "MEMO")
    private String memo;

    @Embedded private DisplayPeriod displayPeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVE_YN")
    private Yn activeYn;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "LINK_URL")
    private String linkUrl;

    public void setDisplayPeriod(DisplayPeriod displayPeriod) {
        this.displayPeriod = displayPeriod;
    }
}
