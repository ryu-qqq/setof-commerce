package com.connectly.partnerAdmin.auth.controller;


import com.connectly.partnerAdmin.auth.dto.*;
import com.connectly.partnerAdmin.auth.service.AdminCommandService;
import com.connectly.partnerAdmin.auth.service.AdminQueryService;
import com.connectly.partnerAdmin.auth.service.AuthTokenGenerateService;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_END_POINT_V1)
public class AuthController {

    private final AuthTokenGenerateService authTokenGenerateService;
    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;

    @PostMapping("/auth/authentication")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> getAccessToken(@RequestBody @Validated CreateAuthToken createAuthToken){
        return ResponseEntity.ok(ApiResponse.success(authTokenGenerateService.generateToken(createAuthToken)));
    }

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<Long>> insertUser(@RequestBody @Validated AdministratorsInsertRequestDto administratorsInsertRequestDto){
        return ResponseEntity.ok(ApiResponse.success(adminCommandService.insert(administratorsInsertRequestDto)));
    }

    @PutMapping("/auth/{authId}")
    public ResponseEntity<ApiResponse<Long>> getAccessToken(@PathVariable("authId") long authId, @RequestBody AdministratorsInsertRequestDto administratorsInsertRequestDto){
        return ResponseEntity.ok(ApiResponse.success(adminCommandService.update(authId, administratorsInsertRequestDto)));
    }

    @GetMapping("/auth")
    public ResponseEntity<ApiResponse<CustomPageable<AdministratorResponse>>> getAdmins(Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(adminQueryService.fetchAdmins(pageable)));
    }

    @GetMapping("/auth/admin-validation")
    public ResponseEntity<ApiResponse<Boolean>> getAdminValidation(@ModelAttribute AdminValidation adminValidation){
        return ResponseEntity.ok(ApiResponse.success(adminQueryService.fetchAdminValidation(adminValidation)));
    }

    @GetMapping("/auth/{sellerId}")
    public ResponseEntity<ApiResponse<CustomPageable<AdministratorResponse>>> getAdminsBySellerId(@PathVariable long sellerId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(adminQueryService.fetchAdminsBySellerId(sellerId, pageable)));
    }

    @PutMapping("/auth/approval-status")
    public ResponseEntity<ApiResponse<List<Long>>> updateApprovalStatus(@RequestBody AdministratorsApprovalStatusRequestDto administratorsApprovalStatusRequestDto){
        return ResponseEntity.ok(ApiResponse.success(adminCommandService.updateStatus(administratorsApprovalStatusRequestDto)));
    }

}