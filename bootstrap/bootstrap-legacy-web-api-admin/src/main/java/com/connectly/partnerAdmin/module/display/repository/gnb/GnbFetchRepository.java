package com.connectly.partnerAdmin.module.display.repository.gnb;

import com.connectly.partnerAdmin.module.display.dto.gnb.filter.GnbFilter;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.entity.gnb.Gnb;

import java.util.List;

public interface GnbFetchRepository {

    List<GnbResponse> fetchGnbs(GnbFilter filter);
    List<Gnb> fetchGnbEntities(List<Long> gnbIds);
}
