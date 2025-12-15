package com.setof.connectly.module.display.repository.gnb;

import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import java.util.List;

public interface GnbFindRepository {

    List<GnbResponse> fetchGnbs();
}
