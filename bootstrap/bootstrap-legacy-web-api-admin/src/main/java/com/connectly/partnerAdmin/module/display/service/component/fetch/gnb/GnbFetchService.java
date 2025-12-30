package com.connectly.partnerAdmin.module.display.service.component.fetch.gnb;

import com.connectly.partnerAdmin.module.display.dto.gnb.filter.GnbFilter;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.entity.gnb.Gnb;

import java.util.List;

public interface GnbFetchService {

    List<GnbResponse> fetchGnbs(GnbFilter filter);

    List<Gnb> fetchGnbEntities(List<Long> gnbIds);
}
