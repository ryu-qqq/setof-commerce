package com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.response.GnbV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.mapper.GnbAdminV2ApiMapper;
import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import com.ryuqq.setof.application.gnb.port.in.query.GetAllGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.query.GetGnbUseCase;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GNB Admin Query Controller
 *
 * <p>CQRS Query 담당: 조회 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "GNB Admin", description = "GNB 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Gnbs.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class GnbAdminQueryController {

    private final GetGnbUseCase getGnbUseCase;
    private final GetAllGnbUseCase getAllGnbUseCase;
    private final GnbAdminV2ApiMapper mapper;

    public GnbAdminQueryController(
            GetGnbUseCase getGnbUseCase,
            GetAllGnbUseCase getAllGnbUseCase,
            GnbAdminV2ApiMapper mapper) {
        this.getGnbUseCase = getGnbUseCase;
        this.getAllGnbUseCase = getAllGnbUseCase;
        this.mapper = mapper;
    }

    /**
     * 전체 GNB 목록 조회
     *
     * @return GNB 목록
     */
    @Operation(summary = "전체 GNB 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<GnbV2ApiResponse>>> getAll() {
        List<GnbResponse> responses = getAllGnbUseCase.execute();
        return ResponseEntity.ok(ApiResponse.ofSuccess(mapper.toApiResponses(responses)));
    }

    /**
     * GNB 상세 조회
     *
     * @param gnbId GNB ID
     * @return GNB 상세 정보
     */
    @Operation(summary = "GNB 상세 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Gnbs.ID_PATH)
    public ResponseEntity<ApiResponse<GnbV2ApiResponse>> getById(
            @Parameter(description = "GNB ID", required = true) @PathVariable Long gnbId) {
        GnbResponse response = getGnbUseCase.execute(GnbId.of(gnbId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(mapper.toApiResponse(response)));
    }
}
