package com.connectly.partnerAdmin.module.display.dto.gnb;


import com.connectly.partnerAdmin.module.display.entity.embedded.GnbDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateGnb {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long gnbId;
    private GnbDetails gnbDetails;

}
