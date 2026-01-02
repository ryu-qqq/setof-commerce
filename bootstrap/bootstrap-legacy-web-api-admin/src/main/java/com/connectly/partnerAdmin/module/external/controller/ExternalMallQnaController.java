package com.connectly.partnerAdmin.module.external.controller;

import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.external.entity.ExternalQna;
import com.connectly.partnerAdmin.module.external.service.qna.ExternalQnaRegistrationService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_AUTHORITY_MASTER;


@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ExternalMallQnaController<T extends ExMallQna> {

    private final ExternalQnaRegistrationService externalQnaRegistrationService;

    @PostMapping("/external/qna")
    public ResponseEntity<ApiResponse<ExternalQna>> interLockingOrders(@RequestBody T t){
        return ResponseEntity.ok(ApiResponse.success(externalQnaRegistrationService.syncQna(t)));
    }

}
