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

    @Column(name = "memo")
    private String memo;

    @Embedded private DisplayPeriod displayPeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_yn")
    private Yn activeYn;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    public void setDisplayPeriod(DisplayPeriod displayPeriod) {
        this.displayPeriod = displayPeriod;
    }
}
