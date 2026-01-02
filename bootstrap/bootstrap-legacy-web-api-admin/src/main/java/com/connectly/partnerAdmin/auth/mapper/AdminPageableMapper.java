package com.connectly.partnerAdmin.auth.mapper;

import com.connectly.partnerAdmin.auth.dto.AdministratorResponse;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminPageableMapper {

    public CustomPageable<AdministratorResponse> toAdministratorResponse(List<AdministratorResponse> administratorResponses, Pageable pageable, long total) {
        Long lastDomainId = administratorResponses.isEmpty() ? null : administratorResponses.getLast().getId();
        return new CustomPageable<>(administratorResponses, pageable, total, lastDomainId);
    }

}
