package com.setof.connectly.module.mileage.controller;

import com.setof.connectly.module.mileage.dto.filter.MileageFilter;
import com.setof.connectly.module.mileage.dto.page.MileagePage;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto;
import com.setof.connectly.module.user.service.mileage.UserMileageFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
@RestController
@RequestMapping("/api/v1/mileage")
@RequiredArgsConstructor
public class MileageController {

    private final UserMileageFindService userMileageFindService;

    @GetMapping("/my-page/mileage-histories")
    public ResponseEntity<ApiResponse<MileagePage<UserMileageHistoryDto>>> fetchMileageHistories(
            @ModelAttribute MileageFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        userMileageFindService.fetchMyMileageHistories(filter, pageable)));
    }
}
