package com.connectly.partnerAdmin.auth.service;

import com.connectly.partnerAdmin.auth.dto.AdminValidation;
import com.connectly.partnerAdmin.auth.dto.AdministratorResponse;
import com.connectly.partnerAdmin.auth.mapper.AdminPageableMapper;
import com.connectly.partnerAdmin.auth.repository.AdministratorsQueryRepository;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminQueryService {

    private final AdminPageableMapper adminPageableMapper;
    private final AdministratorsQueryRepository administratorsQueryRepository;


    @Transactional(readOnly = true)
    public CustomPageable<AdministratorResponse> fetchAdmins(Pageable pageable){
        List<AdministratorResponse> administratorResponses = administratorsQueryRepository.fetchAdministrators(pageable);
        long count = administratorsQueryRepository.fetchAdminCount();
        return adminPageableMapper.toAdministratorResponse(administratorResponses, pageable, count);
    }

    @Transactional(readOnly = true)
    public CustomPageable<AdministratorResponse> fetchAdminsBySellerId(long sellerId, Pageable pageable){
        List<AdministratorResponse> administratorResponses = administratorsQueryRepository.fetchAdministratorBySellerId(sellerId, pageable);
        long count = administratorsQueryRepository.fetchAdminBySellerIdCount(sellerId);
        return adminPageableMapper.toAdministratorResponse(administratorResponses, pageable, count);
    }

    public Boolean fetchAdminValidation(AdminValidation adminValidation) {
        return administratorsQueryRepository.fetchAdminValidation(adminValidation).isPresent();
    }
}
