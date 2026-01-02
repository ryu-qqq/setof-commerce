package com.connectly.partnerAdmin.module.display.service.component.query.gnb;

import com.connectly.partnerAdmin.module.display.dto.gnb.CreateGnb;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.dto.gnb.UpdateGnb;
import com.connectly.partnerAdmin.module.display.entity.gnb.Gnb;
import com.connectly.partnerAdmin.module.display.mapper.gnb.GnbMapper;
import com.connectly.partnerAdmin.module.display.repository.gnb.GnbRepository;
import com.connectly.partnerAdmin.module.display.service.component.fetch.gnb.GnbFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class GnbQueryServiceImpl implements GnbQueryService{

    private final GnbFetchService gnbFetchService;

    private final GnbRedisQueryService gnbRedisQueryService;
    private final GnbRepository gnbRepository;
    private final GnbMapper gnbMapper;

    @Override
    public List<GnbResponse> createGnbs(UpdateGnb updateGnb) {
        List<CreateGnb> createGnbs = updateGnb.getToUpdateGnbs();
        List<Gnb> gnbs = createGnbs.stream()
                .filter(createGnb -> createGnb.getGnbId() == null)
                .map(gnbMapper::toEntity)
                .collect(Collectors.toList());

        List<Gnb> savedGnbs = gnbRepository.saveAll(gnbs);

        List<CreateGnb> toUpdateGnbs = createGnbs.stream()
                .filter(createGnb -> createGnb.getGnbId() != null)
                .collect(Collectors.toList());

        if(!toUpdateGnbs.isEmpty()){
            List<Gnb> updateGnbs = updateGnbs(toUpdateGnbs);
            savedGnbs.addAll(updateGnbs);
        }

        if(!updateGnb.getDeleteGnbIds().isEmpty()) deleteGnbs(updateGnb.getDeleteGnbIds());
        if(!toUpdateGnbs.isEmpty() || !updateGnb.getDeleteGnbIds().isEmpty()) deleteGnbInRedis();

        return savedGnbs.stream().map(gnbMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void deleteGnbInRedis(){
        gnbRedisQueryService.deleteGnbInRedis();
    }

    private List<Gnb> updateGnbs(List<CreateGnb> toUpdateGnbs){
        Map<Long, CreateGnb> gnbMap = toUpdateGnbs.stream().collect(Collectors.toMap(CreateGnb::getGnbId, Function.identity()));
        List<Gnb> gnbs = gnbFetchService.fetchGnbEntities(new ArrayList<>(gnbMap.keySet()));

        for(Gnb gnb : gnbs){
            CreateGnb createGnb = gnbMap.get(gnb.getId());
            gnb.updateGnbDetails(createGnb.getGnbDetails());
        }
        return gnbs;

    }

    private void deleteGnbs(List<Long> deleteGnbIds){
        List<Gnb> gnbs = gnbFetchService.fetchGnbEntities(deleteGnbIds);
        gnbs.forEach(Gnb::delete);
    }

}
