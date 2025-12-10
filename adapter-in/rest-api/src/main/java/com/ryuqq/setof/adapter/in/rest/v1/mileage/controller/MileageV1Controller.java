package com.ryuqq.setof.adapter.in.rest.v1.mileage.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.query.MileageV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MyMileageHistoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MyMileageSummaryV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mileage (Legacy V1)", description = "레거시 Mileage API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class MileageV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 내 마일리지 정보 조회", description = "내 마일리지 정보를 조회합니다.")
    @GetMapping(ApiPaths.Mileage.MY_PAGE)
    public ResponseEntity<ApiResponse<MyMileageSummaryV1ApiResponse>> getMileageSummary(
            @AuthenticationPrincipal MemberPrincipal principal) {
        throw new UnsupportedOperationException("마일리지 이력 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 내 마일리지 이력 조회", description = "내 마일리지 이력을 조회합니다.")
    @GetMapping(ApiPaths.Mileage.HISTORIES)
    public ResponseEntity<ApiResponse<PageApiResponse<MyMileageHistoryV1ApiResponse>>>
            getMileageHistories(
                    @AuthenticationPrincipal MemberPrincipal principal,
                    @ModelAttribute MileageV1SearchApiRequest request) {
        throw new UnsupportedOperationException("마일리지 이력 조회 기능은 아직 지원되지 않습니다.");
    }
}
