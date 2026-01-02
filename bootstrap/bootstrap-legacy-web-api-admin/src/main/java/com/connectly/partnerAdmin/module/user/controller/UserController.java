package com.connectly.partnerAdmin.module.user.controller;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.user.core.WebUserContext;
import com.connectly.partnerAdmin.module.user.filter.UserFilter;
import com.connectly.partnerAdmin.module.user.service.UserFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;


@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class UserController {

    private final UserFetchService userFetchService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<CustomPageable<WebUserContext>>> fetchUsers(@ModelAttribute UserFilter filter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(userFetchService.fetchUsers(filter, pageable)));
    }

}
