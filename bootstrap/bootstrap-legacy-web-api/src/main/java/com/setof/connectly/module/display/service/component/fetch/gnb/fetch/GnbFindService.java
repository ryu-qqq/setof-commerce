package com.setof.connectly.module.display.service.component.fetch.gnb.fetch;

import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import java.util.List;

public interface GnbFindService {
    List<GnbResponse> fetchGnbs();
}
