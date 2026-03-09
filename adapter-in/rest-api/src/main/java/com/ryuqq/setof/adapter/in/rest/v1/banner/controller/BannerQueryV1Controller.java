package com.ryuqq.setof.adapter.in.rest.v1.banner.controller;

import com.ryuqq.setof.adapter.in.rest.v1.banner.BannerV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.banner.dto.response.BannerSlideV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.banner.mapper.BannerV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.banner.port.in.BannerSlideQueryUseCase;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.exception.BannerGroupNotFoundException;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BannerQueryV1Controller - 배너 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "배너 조회 V1", description = "배너 슬라이드 조회 V1 Public API")
@RestController
@RequestMapping(BannerV1Endpoints.BANNERS)
public class BannerQueryV1Controller {

    private final BannerSlideQueryUseCase bannerSlideQueryUseCase;
    private final BannerV1ApiMapper mapper;

    public BannerQueryV1Controller(
            BannerSlideQueryUseCase bannerSlideQueryUseCase, BannerV1ApiMapper mapper) {
        this.bannerSlideQueryUseCase = bannerSlideQueryUseCase;
        this.mapper = mapper;
    }

    /**
     * 배너 슬라이드 목록 조회 API.
     *
     * <p>GET /api/v1/content/banner?bannerType=CATEGORY - 배너 타입별 슬라이드 목록 조회.
     *
     * @param bannerType 배너 타입
     * @return 배너 슬라이드 목록
     */
    @Operation(summary = "배너 슬라이드 목록 조회", description = "배너 타입별 전시 중인 배너 슬라이드 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<List<BannerSlideV1ApiResponse>>> getBanners(
            @Parameter(
                            description = "배너 타입",
                            required = true,
                            example = "CATEGORY",
                            schema =
                                    @Schema(
                                            allowableValues = {
                                                "CATEGORY",
                                                "MY_PAGE",
                                                "CART",
                                                "PRODUCT_DETAIL_DESCRIPTION",
                                                "RECOMMEND",
                                                "LOGIN"
                                            }))
                    @RequestParam
                    @NotBlank(message = "bannerType은 필수입니다.")
                    String bannerType) {
        BannerType type = BannerType.valueOf(bannerType);
        List<BannerSlide> slides = bannerSlideQueryUseCase.fetchDisplayBannerSlides(type);
        if (slides.isEmpty()) {
            throw new BannerGroupNotFoundException(bannerType);
        }
        List<BannerSlideV1ApiResponse> response = mapper.toListResponse(slides);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
