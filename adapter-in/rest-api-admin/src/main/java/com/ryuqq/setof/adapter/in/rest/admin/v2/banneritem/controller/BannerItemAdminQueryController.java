package com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.response.BannerItemV2ApiResponse;
import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import com.ryuqq.setof.application.banneritem.port.in.query.GetBannerItemsUseCase;
import com.ryuqq.setof.domain.cms.vo.BannerId;
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
 * BannerItem Admin 조회 Controller
 *
 * <p>CQRS Query 담당: 조회 전용 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "BannerItem Admin", description = "배너 아이템 관리 API")
@RestController
@RequestMapping(ApiV2Paths.BannerItems.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class BannerItemAdminQueryController {

    private final GetBannerItemsUseCase getBannerItemsUseCase;

    public BannerItemAdminQueryController(GetBannerItemsUseCase getBannerItemsUseCase) {
        this.getBannerItemsUseCase = getBannerItemsUseCase;
    }

    /**
     * 배너 아이템 목록 조회
     *
     * @param bannerId 배너 ID
     * @return 배너 아이템 목록
     */
    @Operation(summary = "배너 아이템 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerItemV2ApiResponse>>> getByBannerId(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId) {
        List<BannerItemResponse> responses =
                getBannerItemsUseCase.getActiveByBannerId(BannerId.of(bannerId));
        List<BannerItemV2ApiResponse> apiResponses =
                responses.stream().map(BannerItemV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }
}
