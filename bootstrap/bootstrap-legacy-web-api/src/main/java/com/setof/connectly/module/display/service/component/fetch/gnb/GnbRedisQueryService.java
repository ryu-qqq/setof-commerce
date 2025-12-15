package com.setof.connectly.module.display.service.component.fetch.gnb;

import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import java.util.List;

public interface GnbRedisQueryService {

    void saveGnbsInRedis(List<GnbResponse> gnbResponses);
}
