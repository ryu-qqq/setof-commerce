package com.connectly.partnerAdmin.module.display.dto.gnb;


import com.connectly.partnerAdmin.module.display.entity.embedded.GnbDetails;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GnbResponse {

    private long gnbId;
    private GnbDetails gnbDetails;

    @QueryProjection
    public GnbResponse(long gnbId, GnbDetails gnbDetails) {
        this.gnbId = gnbId;
        this.gnbDetails = gnbDetails;
    }
}
