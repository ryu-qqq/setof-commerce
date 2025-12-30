package com.connectly.partnerAdmin.module.display.mapper.gnb;

import com.connectly.partnerAdmin.module.display.dto.gnb.CreateGnb;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.entity.gnb.Gnb;

public interface GnbMapper {

    Gnb toEntity(CreateGnb createGnb);
    GnbResponse toResponse(Gnb gnb);
}
