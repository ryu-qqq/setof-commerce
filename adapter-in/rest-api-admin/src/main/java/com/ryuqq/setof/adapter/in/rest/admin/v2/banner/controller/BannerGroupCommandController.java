package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.BannerGroupAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.ChangeBannerGroupStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.RegisterBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerSlidesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.response.RegisterBannerGroupApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.mapper.BannerGroupCommandApiMapper;
import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import com.ryuqq.setof.application.banner.port.in.command.ChangeBannerGroupStatusUseCase;
import com.ryuqq.setof.application.banner.port.in.command.RegisterBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.RemoveBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerSlidesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BannerGroupCommandController - 배너 그룹 생성/수정/상태변경/삭제 API.
 *
 * <p>배너 그룹 등록, 수정, 슬라이드 수정, 노출 상태 변경, 소프트 삭제 엔드포인트를 제공합니다.
 *
 * <p>API-CTR-001: Controller는 @RestController로 정의.
 *
 * <p>API-CTR-004: ResponseEntity&lt;ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-002: DELETE 메서드 금지 (소프트 삭제는 PATCH).
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-009: @Valid 어노테이션 필수.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "배너 그룹 관리", description = "배너 그룹 생성/수정/상태변경/삭제 API")
@RestController
@RequestMapping(BannerGroupAdminEndpoints.BANNER_GROUPS)
public class BannerGroupCommandController {

    private final RegisterBannerGroupUseCase registerUseCase;
    private final UpdateBannerGroupUseCase updateUseCase;
    private final UpdateBannerSlidesUseCase updateSlidesUseCase;
    private final ChangeBannerGroupStatusUseCase changeStatusUseCase;
    private final RemoveBannerGroupUseCase removeUseCase;
    private final BannerGroupCommandApiMapper mapper;

    /**
     * BannerGroupCommandController 생성자.
     *
     * @param registerUseCase 배너 그룹 등록 UseCase
     * @param updateUseCase 배너 그룹 수정 UseCase
     * @param updateSlidesUseCase 배너 슬라이드 수정 UseCase
     * @param changeStatusUseCase 배너 그룹 노출 상태 변경 UseCase
     * @param removeUseCase 배너 그룹 소프트 삭제 UseCase
     * @param mapper Command API 매퍼
     */
    public BannerGroupCommandController(
            RegisterBannerGroupUseCase registerUseCase,
            UpdateBannerGroupUseCase updateUseCase,
            UpdateBannerSlidesUseCase updateSlidesUseCase,
            ChangeBannerGroupStatusUseCase changeStatusUseCase,
            RemoveBannerGroupUseCase removeUseCase,
            BannerGroupCommandApiMapper mapper) {
        this.registerUseCase = registerUseCase;
        this.updateUseCase = updateUseCase;
        this.updateSlidesUseCase = updateSlidesUseCase;
        this.changeStatusUseCase = changeStatusUseCase;
        this.removeUseCase = removeUseCase;
        this.mapper = mapper;
    }

    /**
     * 배너 그룹 등록 API.
     *
     * <p>새로운 배너 그룹과 슬라이드를 등록합니다.
     *
     * @param request 등록 요청 DTO
     * @return 생성된 배너 그룹 ID (201 Created)
     */
    @Operation(summary = "배너 그룹 등록", description = "새로운 배너 그룹과 슬라이드를 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<RegisterBannerGroupApiResponse>> register(
            @Valid @RequestBody RegisterBannerGroupApiRequest request) {

        RegisterBannerGroupCommand command = mapper.toCommand(request);
        Long createdId = registerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(new RegisterBannerGroupApiResponse(createdId)));
    }

    /**
     * 배너 그룹 수정 API.
     *
     * <p>배너 그룹 정보(타이틀, 타입, 노출 기간, 활성 여부)를 수정합니다. 슬라이드 수정은 {@code PUT
     * /api/v2/admin/banner-groups/{bannerGroupId}/slides}를 사용하세요.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @param request 수정 요청 DTO
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "배너 그룹 수정", description = "배너 그룹 정보를 수정합니다. 슬라이드 수정은 슬라이드 수정 API를 사용하세요.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배너 그룹을 찾을 수 없음")
    })
    @PutMapping(BannerGroupAdminEndpoints.ID)
    public ResponseEntity<Void> update(
            @Parameter(description = "배너 그룹 ID", required = true)
                    @PathVariable(BannerGroupAdminEndpoints.PATH_BANNER_GROUP_ID)
                    long bannerGroupId,
            @Valid @RequestBody UpdateBannerGroupApiRequest request) {

        UpdateBannerGroupCommand command = mapper.toCommand(bannerGroupId, request);
        updateUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }

    /**
     * 배너 슬라이드 일괄 수정 API.
     *
     * <p>배너 그룹에 속한 슬라이드를 Diff 기반으로 수정합니다. slideId가 null이면 신규 생성, 값이 있으면 기존 수정, 요청에 미포함이면 삭제로 처리됩니다.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @param request 슬라이드 수정 요청 DTO
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "배너 슬라이드 일괄 수정", description = "배너 그룹의 슬라이드를 Diff 기반으로 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배너 그룹을 찾을 수 없음")
    })
    @PutMapping(BannerGroupAdminEndpoints.ID + BannerGroupAdminEndpoints.SLIDES)
    public ResponseEntity<Void> updateSlides(
            @Parameter(description = "배너 그룹 ID", required = true)
                    @PathVariable(BannerGroupAdminEndpoints.PATH_BANNER_GROUP_ID)
                    long bannerGroupId,
            @Valid @RequestBody UpdateBannerSlidesApiRequest request) {

        UpdateBannerSlidesCommand command = mapper.toSlidesCommand(bannerGroupId, request);
        updateSlidesUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }

    /**
     * 배너 그룹 노출 상태 변경 API.
     *
     * <p>배너 그룹의 노출(active) 상태를 변경합니다.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @param request 노출 상태 변경 요청 DTO
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "배너 그룹 노출 상태 변경", description = "배너 그룹의 노출 상태를 변경합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "상태 변경 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배너 그룹을 찾을 수 없음")
    })
    @PatchMapping(BannerGroupAdminEndpoints.ID + BannerGroupAdminEndpoints.ACTIVE_STATUS)
    public ResponseEntity<Void> changeStatus(
            @Parameter(description = "배너 그룹 ID", required = true)
                    @PathVariable(BannerGroupAdminEndpoints.PATH_BANNER_GROUP_ID)
                    long bannerGroupId,
            @Valid @RequestBody ChangeBannerGroupStatusApiRequest request) {

        ChangeBannerGroupStatusCommand command = mapper.toCommand(bannerGroupId, request);
        changeStatusUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }

    /**
     * 배너 그룹 소프트 삭제 API.
     *
     * <p>배너 그룹을 논리 삭제합니다. (API-CTR-002: DELETE 금지, PATCH 사용)
     *
     * @param bannerGroupId 삭제 대상 배너 그룹 ID
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "배너 그룹 삭제", description = "배너 그룹을 논리 삭제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배너 그룹을 찾을 수 없음")
    })
    @PatchMapping(BannerGroupAdminEndpoints.ID + BannerGroupAdminEndpoints.REMOVE)
    public ResponseEntity<Void> remove(
            @Parameter(description = "배너 그룹 ID", required = true)
                    @PathVariable(BannerGroupAdminEndpoints.PATH_BANNER_GROUP_ID)
                    long bannerGroupId) {

        RemoveBannerGroupCommand command = mapper.toCommand(bannerGroupId);
        removeUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }
}
