package com.connectly.partnerAdmin.module.display.mapper.gnb;

import com.connectly.partnerAdmin.module.display.dto.gnb.CreateGnb;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.entity.gnb.Gnb;
import org.springframework.stereotype.Component;

@Component
public class GnbMapperImpl implements GnbMapper{


    @Override
    public Gnb toEntity(CreateGnb createGnb) {
        return Gnb.builder()
                .gnbDetails(createGnb.getGnbDetails())
                .build();
    }

    @Override
    public GnbResponse toResponse(Gnb gnb) {
        return GnbResponse.builder()
                .gnbId(gnb.getId())
                .gnbDetails(gnb.getGnbDetails())
                .build();
    }


}
