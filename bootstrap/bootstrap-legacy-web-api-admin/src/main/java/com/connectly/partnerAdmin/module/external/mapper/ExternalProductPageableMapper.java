package com.connectly.partnerAdmin.module.external.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductInfoDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExternalProductPageableMapper {

    CustomPageable<ExternalProductInfoDto> toExternalProductInfos(List<ExternalProductInfoDto> crawlProductInfos, Pageable pageable, long total);

}
