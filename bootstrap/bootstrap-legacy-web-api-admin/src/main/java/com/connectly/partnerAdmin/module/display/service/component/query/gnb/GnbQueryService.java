package com.connectly.partnerAdmin.module.display.service.component.query.gnb;

import com.connectly.partnerAdmin.module.display.dto.gnb.CreateGnb;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.dto.gnb.UpdateGnb;

import java.util.List;

public interface GnbQueryService {
    List<GnbResponse> createGnbs(UpdateGnb updateGnb);

}
