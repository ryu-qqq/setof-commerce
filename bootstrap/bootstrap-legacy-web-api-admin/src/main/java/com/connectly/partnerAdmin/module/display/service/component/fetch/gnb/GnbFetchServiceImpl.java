package com.connectly.partnerAdmin.module.display.service.component.fetch.gnb;


import com.connectly.partnerAdmin.module.display.dto.gnb.filter.GnbFilter;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.entity.gnb.Gnb;
import com.connectly.partnerAdmin.module.display.repository.gnb.GnbFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GnbFetchServiceImpl implements GnbFetchService {

    private final GnbFetchRepository gnbFetchRepository;


    public List<GnbResponse> fetchGnbs(GnbFilter filter){
        return gnbFetchRepository.fetchGnbs(filter);
    }

    @Override
    public List<Gnb> fetchGnbEntities(List<Long> gnbIds) {
        return gnbFetchRepository.fetchGnbEntities(gnbIds);
    }


}
