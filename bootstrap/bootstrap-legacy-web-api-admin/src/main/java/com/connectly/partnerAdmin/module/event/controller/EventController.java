package com.connectly.partnerAdmin.module.event.controller;

import com.connectly.partnerAdmin.module.event.dto.CreateEvent;
import com.connectly.partnerAdmin.module.event.entity.embedded.EventDetail;
import com.connectly.partnerAdmin.module.event.service.EventQueryService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
public class EventController {
    private final EventQueryService eventQueryService;

    @PostMapping("/event")
    public ResponseEntity<ApiResponse<EventDetail>> enrollEvent(@RequestBody @Validated CreateEvent createEvent){
        return ResponseEntity.ok(ApiResponse.success(eventQueryService.enrollEvent(createEvent)));
    }


}
