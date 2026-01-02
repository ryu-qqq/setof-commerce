package com.connectly.partnerAdmin.auth.service;

import com.connectly.partnerAdmin.auth.dto.AdministratorsApprovalStatusRequestDto;
import com.connectly.partnerAdmin.auth.dto.AdministratorsInsertRequestDto;
import com.connectly.partnerAdmin.auth.entity.AdminAuthGroup;
import com.connectly.partnerAdmin.auth.entity.Administrators;
import com.connectly.partnerAdmin.auth.exception.AuthUserNotFoundException;
import com.connectly.partnerAdmin.auth.repository.AdminAuthGroupRepository;
import com.connectly.partnerAdmin.auth.repository.AdministratorsRepository;
import com.connectly.partnerAdmin.module.seller.exception.SellerNotFoundException;
import com.connectly.partnerAdmin.module.seller.service.SellerFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AdminCommandService {
    private final SellerFetchService sellerFetchService;
    private final AdministratorsRepository administratorsRepository;
    private final AdminAuthGroupRepository adminAuthGroupRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public long insert(AdministratorsInsertRequestDto administratorsInsertRequestDto){
        boolean b = sellerFetchService.fetchHasSellerExist(administratorsInsertRequestDto.getSellerId());
        if(!b) throw new SellerNotFoundException();

        String encodedPassword = passwordEncoder.encode(administratorsInsertRequestDto.getPasswordHash());
        Administrators administratorsEntity = administratorsInsertRequestDto.toAdministratorsEntity(encodedPassword);
        Administrators savedAdministrators = administratorsRepository.save(administratorsEntity);
        AdminAuthGroup adminAuthGroup = new AdminAuthGroup(savedAdministrators.getId(), administratorsInsertRequestDto.getRoleType());
        adminAuthGroupRepository.save(adminAuthGroup);

        return savedAdministrators.getId();
    }

    public long update(long adminId, AdministratorsInsertRequestDto administratorsInsertRequestDto){
        Administrators administrators = administratorsRepository.findById(adminId).orElseThrow(AuthUserNotFoundException::new);
        String encodedPassword = passwordEncoder.encode(administratorsInsertRequestDto.getPasswordHash());
        administrators.update(administratorsInsertRequestDto.toAdministratorsEntity(encodedPassword));

        AdminAuthGroup adminAuthGroup = adminAuthGroupRepository.findById(adminId).orElseThrow(AuthUserNotFoundException::new);
        adminAuthGroup.update(administratorsInsertRequestDto.getRoleType());
        return adminId;
    }


    @Transactional
    public List<Long> updateStatus(AdministratorsApprovalStatusRequestDto administratorsApprovalStatusRequestDto){
        List<Administrators> admins = administratorsRepository.findAllById(administratorsApprovalStatusRequestDto.adminIds());
        admins.forEach(administrators -> administrators.updateStatus(administratorsApprovalStatusRequestDto.approvalStatus()));
        return administratorsApprovalStatusRequestDto.adminIds();
    }

}
