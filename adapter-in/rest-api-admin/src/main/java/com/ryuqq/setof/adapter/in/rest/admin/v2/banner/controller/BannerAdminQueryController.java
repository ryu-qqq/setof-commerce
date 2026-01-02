package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.query.SearchBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.response.BannerV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.mapper.BannerAdminV2ApiMapper;
import com.ryuqq.setof.application.banner.dto.query.SearchBannerQuery;
import com.ryuqq.setof.application.banner.dto.response.BannerResponse;
import com.ryuqq.setof.application.banner.port.in.query.GetBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.query.SearchBannerUseCase;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Banner Admin 조회 Controller
 *
 * <p>CQRS Query 담당: 조회 전용 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Banner Admin", description = "배너 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Banners.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class BannerAdminQueryController {

    private final GetBannerUseCase getBannerUseCase;
    private final SearchBannerUseCase searchBannerUseCase;
    private final BannerAdminV2ApiMapper mapper;

    public BannerAdminQueryController(
            GetBannerUseCase getBannerUseCase,
            SearchBannerUseCase searchBannerUseCase,
            BannerAdminV2ApiMapper mapper) {
        this.getBannerUseCase = getBannerUseCase;
        this.searchBannerUseCase = searchBannerUseCase;
        this.mapper = mapper;
    }

    /**
     * 배너 목록 조회
     *
     * @param request 검색 조건
     * @return 배너 목록
     */
    @Operation(summary = "배너 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerV2ApiResponse>>> search(
            @ParameterObject SearchBannerV2ApiRequest request) {
        SearchBannerQuery query = mapper.toSearchQuery(request);
        List<BannerResponse> responses = searchBannerUseCase.execute(query);
        List<BannerV2ApiResponse> apiResponses =
                responses.stream().map(BannerV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    /**
     * 배너 단건 조회
     *
     * @param bannerId 배너 ID
     * @return 배너 정보
     */
    @Operation(summary = "배너 단건 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Banners.ID_PATH)
    public ResponseEntity<ApiResponse<BannerV2ApiResponse>> getById(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId) {
        BannerResponse response = getBannerUseCase.execute(BannerId.of(bannerId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(BannerV2ApiResponse.from(response)));
    }
}
