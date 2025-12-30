package com.connectly.partnerAdmin.module.external.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductInfoDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalProductPageableMapperImpl implements ExternalProductPageableMapper {

    @Override
    public CustomPageable<ExternalProductInfoDto> toExternalProductInfos(List<ExternalProductInfoDto> externalProductInfos, Pageable pageable, long total) {
        Long lastDomainId = externalProductInfos.isEmpty() ? null : externalProductInfos.getLast().getExternalId();
        return new CustomPageable<>(externalProductInfos, pageable, total, lastDomainId);
    }

}
