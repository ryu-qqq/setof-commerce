package com.setof.connectly.module.display.service.component.fetch.gnb.fetch;

import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import com.setof.connectly.module.display.repository.gnb.GnbFindRepository;
import com.setof.connectly.module.display.service.component.fetch.gnb.GnbRedisQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GnbFindServiceImpl implements GnbFindService {

    private final GnbFindRepository gnbFindRepository;
    private final GnbRedisQueryService gnbRedisQueryService;

    @Override
    public List<GnbResponse> fetchGnbs() {
        List<GnbResponse> gnbResponses = gnbFindRepository.fetchGnbs();
        gnbRedisQueryService.saveGnbsInRedis(gnbResponses);
        return gnbResponses;
    }
}
