package com.setof.connectly.module.display.dto.gnb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GnbResponse {

    private long gnbId;
    private String title;
    private String linkUrl;
    @JsonIgnore private int displayOrder;
    @JsonIgnore private DisplayPeriod displayPeriod;

    @QueryProjection
    public GnbResponse(
            long gnbId,
            String title,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod) {
        this.gnbId = gnbId;
        this.title = title;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayPeriod = displayPeriod;
    }
}
