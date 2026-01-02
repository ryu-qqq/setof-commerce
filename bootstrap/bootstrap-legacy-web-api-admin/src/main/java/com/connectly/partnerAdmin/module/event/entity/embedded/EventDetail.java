package com.connectly.partnerAdmin.module.event.entity.embedded;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import jakarta.persistence.*;
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

    @Embedded
    private DisplayPeriod displayPeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVE_YN")
    private Yn activeYn;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "LINK_URL")
    private String linkUrl;


}
