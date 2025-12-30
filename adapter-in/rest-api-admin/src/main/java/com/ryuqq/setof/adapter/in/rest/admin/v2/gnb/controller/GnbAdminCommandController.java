package com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.CreateGnbV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.UpdateGnbV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.mapper.GnbAdminV2ApiMapper;
import com.ryuqq.setof.application.gnb.dto.command.CreateGnbCommand;
import com.ryuqq.setof.application.gnb.dto.command.DeleteGnbCommand;
import com.ryuqq.setof.application.gnb.dto.command.UpdateGnbCommand;
import com.ryuqq.setof.application.gnb.port.in.command.CreateGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.command.DeleteGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.command.UpdateGnbUseCase;
import com.ryuqq.setof.domain.cms.vo.GnbId;
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
 * GNB Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "GNB Admin", description = "GNB 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Gnbs.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class GnbAdminCommandController {

    private final CreateGnbUseCase createGnbUseCase;
    private final UpdateGnbUseCase updateGnbUseCase;
    private final DeleteGnbUseCase deleteGnbUseCase;
    private final GnbAdminV2ApiMapper mapper;

    public GnbAdminCommandController(
            CreateGnbUseCase createGnbUseCase,
            UpdateGnbUseCase updateGnbUseCase,
            DeleteGnbUseCase deleteGnbUseCase,
            GnbAdminV2ApiMapper mapper) {
        this.createGnbUseCase = createGnbUseCase;
        this.updateGnbUseCase = updateGnbUseCase;
        this.deleteGnbUseCase = deleteGnbUseCase;
        this.mapper = mapper;
    }

    /**
     * GNB 생성
     *
     * @param request 생성 요청
     * @return 생성된 GNB ID
     */
    @Operation(summary = "GNB 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateGnbV2ApiRequest request) {
        CreateGnbCommand command = mapper.toCreateCommand(request);
        Long gnbId = createGnbUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(gnbId));
    }

    /**
     * GNB 수정
     *
     * @param gnbId GNB ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "GNB 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.Gnbs.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "GNB ID", required = true) @PathVariable Long gnbId,
            @Valid @RequestBody UpdateGnbV2ApiRequest request) {
        UpdateGnbCommand command = mapper.toUpdateCommand(gnbId, request);
        updateGnbUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * GNB 삭제
     *
     * @param gnbId GNB ID
     * @return 성공 응답
     */
    @Operation(summary = "GNB 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.Gnbs.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "GNB ID", required = true) @PathVariable Long gnbId) {
        deleteGnbUseCase.execute(new DeleteGnbCommand(GnbId.of(gnbId)));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
