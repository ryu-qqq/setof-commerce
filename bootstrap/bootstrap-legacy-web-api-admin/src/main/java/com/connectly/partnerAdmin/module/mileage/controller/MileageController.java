package com.connectly.partnerAdmin.module.mileage.controller;


import com.connectly.partnerAdmin.module.mileage.service.MileageManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_AUTHORITY_MASTER;

@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_END_POINT_V1)
public class MileageController {

    private final MileageManageService mileageManageService;

    @PostMapping("/mileage/expire")
    public void expireMileage(){
        mileageManageService.expireMileage();
    }

}
