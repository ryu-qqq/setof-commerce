package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateBannerItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateBannerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateBannerDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.BannerCommandV1ApiMapper;
import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import com.ryuqq.setof.application.banner.port.in.command.ChangeBannerGroupStatusUseCase;
import com.ryuqq.setof.application.banner.port.in.command.RegisterBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerSlidesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BannerCommandV1Controller - 배너 그룹 등록/수정/상태변경 v1 API.
 *
 * <p>레거시 배너 Command API와 동일한 URL, HTTP 메서드, 요청/응답 구조를 유지합니다.
 *
 * <p>레거시 호환 URL:
 *
 * <ul>
 *   <li>POST /api/v1/content/banner — 배너 그룹 등록
 *   <li>PUT /api/v1/content/banner/{bannerId} — 배너 그룹 수정
 *   <li>POST /api/v1/content/banner/items — 슬라이드 일괄 등록/수정/삭제
 *   <li>PATCH /api/v1/content/banner/{bannerId}/display-status — 노출 상태 변경
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "배너 그룹 관리 v1", description = "배너 그룹 등록/수정/상태변경 v1 API (레거시 호환)")
@RestController
@RequestMapping("/api/v1/content")
public class BannerCommandV1Controller {

    private final BannerCommandV1ApiMapper mapper;
    private final RegisterBannerGroupUseCase registerBannerGroupUseCase;
    private final UpdateBannerGroupUseCase updateBannerGroupUseCase;
    private final UpdateBannerSlidesUseCase updateBannerSlidesUseCase;
    private final ChangeBannerGroupStatusUseCase changeBannerGroupStatusUseCase;

    /**
     * BannerCommandV1Controller 생성자.
     *
     * @param mapper v1 Command API 매퍼
     * @param registerBannerGroupUseCase 배너 그룹 등록 UseCase
     * @param updateBannerGroupUseCase 배너 그룹 수정 UseCase
     * @param updateBannerSlidesUseCase 배너 슬라이드 일괄 수정 UseCase
     * @param changeBannerGroupStatusUseCase 배너 그룹 노출 상태 변경 UseCase
     */
    public BannerCommandV1Controller(
            BannerCommandV1ApiMapper mapper,
            RegisterBannerGroupUseCase registerBannerGroupUseCase,
            UpdateBannerGroupUseCase updateBannerGroupUseCase,
            UpdateBannerSlidesUseCase updateBannerSlidesUseCase,
            ChangeBannerGroupStatusUseCase changeBannerGroupStatusUseCase) {
        this.mapper = mapper;
        this.registerBannerGroupUseCase = registerBannerGroupUseCase;
        this.updateBannerGroupUseCase = updateBannerGroupUseCase;
        this.updateBannerSlidesUseCase = updateBannerSlidesUseCase;
        this.changeBannerGroupStatusUseCase = changeBannerGroupStatusUseCase;
    }

    /**
     * 배너 그룹 등록 API.
     *
     * <p>슬라이드 없이 그룹만 등록합니다. 슬라이드는 POST /banner/items로 별도 등록합니다.
     *
     * @param request 배너 그룹 등록 요청 DTO
     * @return 생성된 배너 그룹 ID
     */
    @Operation(
            summary = "배너 그룹 등록",
            description = "배너 그룹을 등록합니다. 슬라이드는 POST /banner/items로 별도 등록하세요.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping("/banner")
    public ResponseEntity<V1ApiResponse<Long>> enrollBanner(
            @Validated @RequestBody CreateBannerV1ApiRequest request) {
        RegisterBannerGroupCommand command = mapper.toRegisterCommand(request);
        Long bannerId = registerBannerGroupUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(bannerId));
    }

    /**
     * 배너 그룹 수정 API.
     *
     * <p>슬라이드 없이 그룹 정보만 수정합니다. 슬라이드 수정은 POST /banner/items로 별도 처리합니다.
     *
     * @param bannerId 배너 그룹 ID
     * @param request 배너 그룹 수정 요청 DTO
     * @return 성공 응답
     */
    @Operation(
            summary = "배너 그룹 수정",
            description = "배너 그룹 정보를 수정합니다. 슬라이드 수정은 POST /banner/items를 사용하세요.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배너 그룹을 찾을 수 없음")
    })
    @PutMapping("/banner/{bannerId}")
    public ResponseEntity<V1ApiResponse<Void>> updateBanner(
            @Parameter(description = "배너 그룹 ID", required = true) @PathVariable long bannerId,
            @Validated @RequestBody CreateBannerV1ApiRequest request) {
        UpdateBannerGroupCommand command = mapper.toUpdateCommand(bannerId, request);
        updateBannerGroupUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success());
    }

    /**
     * 배너 슬라이드 일괄 등록/수정/삭제 API.
     *
     * <p>bannerItemId가 null이면 신규 생성, 값이 있으면 수정, 기존 아이템이 요청에 없으면 삭제됩니다.
     *
     * @param requests 배너 슬라이드 요청 DTO 목록
     * @return 요청 DTO 목록 그대로 반환
     */
    @Operation(
            summary = "배너 슬라이드 일괄 등록/수정/삭제",
            description =
                    "배너 슬라이드를 일괄 처리합니다. bannerItemId가 null이면 신규, 값이 있으면 수정, 요청에 없는 기존 아이템은 삭제됩니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "처리 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping("/banner/items")
    public ResponseEntity<V1ApiResponse<List<CreateBannerItemV1ApiRequest>>> enrollBannerItems(
            @RequestBody @Valid List<CreateBannerItemV1ApiRequest> requests) {
        UpdateBannerSlidesCommand command = mapper.toUpdateSlidesCommand(requests);
        updateBannerSlidesUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(requests));
    }

    /**
     * 배너 그룹 노출 상태 변경 API.
     *
     * @param bannerId 배너 그룹 ID
     * @param request 노출 상태 변경 요청 DTO
     * @return 성공 응답
     */
    @Operation(summary = "배너 그룹 노출 상태 변경", description = "배너 그룹의 전시 여부(displayYn)를 변경합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "상태 변경 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배너 그룹을 찾을 수 없음")
    })
    @PatchMapping("/banner/{bannerId}/display-status")
    public ResponseEntity<V1ApiResponse<Void>> updateBannerDisplayYn(
            @Parameter(description = "배너 그룹 ID", required = true) @PathVariable long bannerId,
            @RequestBody UpdateBannerDisplayYnV1ApiRequest request) {
        ChangeBannerGroupStatusCommand command = mapper.toChangeStatusCommand(bannerId, request);
        changeBannerGroupStatusUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success());
    }
}
