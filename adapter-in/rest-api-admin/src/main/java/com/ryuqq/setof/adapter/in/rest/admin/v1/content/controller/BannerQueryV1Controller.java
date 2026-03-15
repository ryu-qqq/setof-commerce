package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchBannerItemsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchBannersV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerItemV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.BannerQueryV1ApiMapper;
import com.ryuqq.setof.application.banner.port.in.query.GetBannerGroupDetailUseCase;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BannerQueryV1Controller - 배너 조회 v1 API.
 *
 * <p>레거시 배너 조회 API와 동일한 URL, HTTP 메서드, 요청/응답 구조를 유지합니다.
 *
 * <p>레거시 호환 URL:
 *
 * <ul>
 *   <li>GET /api/v1/content/banners — 배너 그룹 목록 조회 (검색/페이징)
 *   <li>GET /api/v1/content/banner/{bannerId} — 배너 아이템(슬라이드) 목록 조회
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "배너 조회 v1", description = "배너 그룹/아이템 조회 v1 API (레거시 호환)")
@RestController
@RequestMapping("/api/v1/content")
public class BannerQueryV1Controller {

    private final GetBannerGroupDetailUseCase getBannerGroupDetailUseCase;
    private final BannerQueryV1ApiMapper queryMapper;

    /**
     * BannerQueryV1Controller 생성자.
     *
     * @param getBannerGroupDetailUseCase 배너 그룹 상세 조회 UseCase
     * @param queryMapper 배너 Query API 매퍼
     */
    public BannerQueryV1Controller(
            GetBannerGroupDetailUseCase getBannerGroupDetailUseCase,
            BannerQueryV1ApiMapper queryMapper) {
        this.getBannerGroupDetailUseCase = getBannerGroupDetailUseCase;
        this.queryMapper = queryMapper;
    }

    /**
     * 배너 그룹 목록 조회 API.
     *
     * <p>TODO: SearchBannerGroupsUseCase 구현 후 연동. 현재는 검색/필터/페이징을 지원하는 Application UseCase가 없습니다.
     *
     * @param filter 검색 필터 및 페이징 요청 DTO
     * @return 미구현 (UnsupportedOperationException)
     */
    @Operation(
            summary = "배너 그룹 목록 조회 (미구현)",
            description = "TODO: SearchBannerGroupsUseCase 구현 후 연동 예정.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "미구현")
    })
    @GetMapping("/banners")
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<BannerGroupV1ApiResponse>>>
            getBanners(@ModelAttribute SearchBannersV1ApiRequest filter) {

        // TODO: SearchBannerGroupsUseCase 구현 후 연동
        throw new UnsupportedOperationException(
                "배너 그룹 목록 조회 UseCase가 아직 구현되지 않았습니다.");
    }

    /**
     * 배너 아이템(슬라이드) 목록 조회 API.
     *
     * <p>배너 그룹 ID로 해당 그룹의 슬라이드 목록을 조회합니다.
     *
     * @param bannerId 배너 그룹 ID
     * @param filter 조회 필터 (시작일, 종료일)
     * @return 배너 아이템 목록
     */
    @Operation(summary = "배너 아이템 목록 조회", description = "배너 그룹 ID로 슬라이드 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배너 그룹을 찾을 수 없음")
    })
    @GetMapping("/banner/{bannerId}")
    public ResponseEntity<V1ApiResponse<List<BannerItemV1ApiResponse>>> getBannerItems(
            @Parameter(description = "배너 그룹 ID", required = true) @PathVariable long bannerId,
            @ModelAttribute SearchBannerItemsV1ApiRequest filter) {

        BannerGroup group = getBannerGroupDetailUseCase.execute(bannerId);

        List<BannerItemV1ApiResponse> items =
                group.slides().stream()
                        .map(slide -> queryMapper.toBannerItemResponse(slide, group.bannerType()))
                        .toList();

        return ResponseEntity.ok(V1ApiResponse.success(items));
    }
}
