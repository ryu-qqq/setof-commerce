package com.ryuqq.setof.adapter.in.rest.v1.mileage.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.MileageV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.request.SearchMileageHistoriesV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MileageHistoryPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.mapper.MileageV1ApiMapper;
import com.ryuqq.setof.application.mileage.dto.query.MileageHistorySearchParams;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryPageResult;
import com.ryuqq.setof.application.mileage.port.in.query.GetMileageHistoriesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * MileageQueryV1Controller - 마일리지 조회 V1 Public API.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "마일리지 조회 V1", description = "마일리지 조회 V1 Public API (인증 필요)")
@RestController
public class MileageQueryV1Controller {

    private final GetMileageHistoriesUseCase getMileageHistoriesUseCase;
    private final MileageV1ApiMapper mapper;

    public MileageQueryV1Controller(
            GetMileageHistoriesUseCase getMileageHistoriesUseCase, MileageV1ApiMapper mapper) {
        this.getMileageHistoriesUseCase = getMileageHistoriesUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "마일리지 이력 조회", description = "인증된 사용자의 마일리지 이력을 오프셋 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(MileageV1Endpoints.MILEAGE_HISTORIES)
    public ResponseEntity<V1ApiResponse<MileageHistoryPageV1ApiResponse>> getMileageHistories(
            @AuthenticatedUserId Long userId,
            @ModelAttribute SearchMileageHistoriesV1ApiRequest request) {

        MileageHistorySearchParams params = mapper.toSearchParams(userId, request);
        MileageHistoryPageResult result = getMileageHistoriesUseCase.execute(params);
        MileageHistoryPageV1ApiResponse response = mapper.toPageResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
