package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.CreateBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.mapper.BannerAdminV2ApiMapper;
import com.ryuqq.setof.application.banner.dto.command.ActivateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.CreateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.DeactivateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.DeleteBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerCommand;
import com.ryuqq.setof.application.banner.port.in.command.ActivateBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.CreateBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.DeactivateBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.DeleteBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerUseCase;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Banner Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제, 활성화/비활성화 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Banner Admin", description = "배너 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Banners.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class BannerAdminCommandController {

    private final CreateBannerUseCase createBannerUseCase;
    private final UpdateBannerUseCase updateBannerUseCase;
    private final DeleteBannerUseCase deleteBannerUseCase;
    private final ActivateBannerUseCase activateBannerUseCase;
    private final DeactivateBannerUseCase deactivateBannerUseCase;
    private final BannerAdminV2ApiMapper mapper;

    public BannerAdminCommandController(
            CreateBannerUseCase createBannerUseCase,
            UpdateBannerUseCase updateBannerUseCase,
            DeleteBannerUseCase deleteBannerUseCase,
            ActivateBannerUseCase activateBannerUseCase,
            DeactivateBannerUseCase deactivateBannerUseCase,
            BannerAdminV2ApiMapper mapper) {
        this.createBannerUseCase = createBannerUseCase;
        this.updateBannerUseCase = updateBannerUseCase;
        this.deleteBannerUseCase = deleteBannerUseCase;
        this.activateBannerUseCase = activateBannerUseCase;
        this.deactivateBannerUseCase = deactivateBannerUseCase;
        this.mapper = mapper;
    }

    /**
     * 배너 생성
     *
     * @param request 생성 요청
     * @return 생성된 배너 ID
     */
    @Operation(summary = "배너 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateBannerV2ApiRequest request) {
        CreateBannerCommand command = mapper.toCreateCommand(request);
        Long bannerId = createBannerUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(bannerId));
    }

    /**
     * 배너 수정
     *
     * @param bannerId 배너 ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "배너 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.Banners.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId,
            @Valid @RequestBody UpdateBannerV2ApiRequest request) {
        UpdateBannerCommand command = mapper.toUpdateCommand(bannerId, request);
        updateBannerUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 배너 활성화
     *
     * @param bannerId 배너 ID
     * @return 성공 응답
     */
    @Operation(summary = "배너 활성화")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "활성화 성공")
            })
    @PostMapping(ApiV2Paths.Banners.ACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> activate(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId) {
        activateBannerUseCase.execute(new ActivateBannerCommand(bannerId));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 배너 비활성화
     *
     * @param bannerId 배너 ID
     * @return 성공 응답
     */
    @Operation(summary = "배너 비활성화")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "비활성화 성공")
            })
    @PostMapping(ApiV2Paths.Banners.DEACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> deactivate(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId) {
        deactivateBannerUseCase.execute(new DeactivateBannerCommand(bannerId));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 배너 삭제
     *
     * @param bannerId 배너 ID
     * @return 성공 응답
     */
    @Operation(summary = "배너 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.Banners.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId) {
        deleteBannerUseCase.execute(new DeleteBannerCommand(BannerId.of(bannerId)));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
