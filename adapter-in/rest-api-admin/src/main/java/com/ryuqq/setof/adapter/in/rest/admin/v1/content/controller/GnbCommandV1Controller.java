package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateGnbV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateGnbV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.GnbCommandV1ApiMapper;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.port.in.command.RegisterNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.port.in.command.RemoveNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.port.in.command.UpdateNavigationMenuUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GnbCommandV1Controller - GNB 등록/수정/삭제 일괄 처리 v1 API.
 *
 * <p>레거시 GNB Command API와 동일한 URL, HTTP 메서드, 요청/응답 구조를 유지합니다.
 *
 * <p>레거시 호환 URL:
 *
 * <ul>
 *   <li>POST /api/v1/content/gnbs — GNB 등록/수정/삭제 일괄 처리
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "GNB 관리 v1", description = "GNB 등록/수정/삭제 일괄 처리 v1 API (레거시 호환)")
@RestController
@RequestMapping("/api/v1/content")
public class GnbCommandV1Controller {

    private final GnbCommandV1ApiMapper mapper;
    private final RegisterNavigationMenuUseCase registerNavigationMenuUseCase;
    private final UpdateNavigationMenuUseCase updateNavigationMenuUseCase;
    private final RemoveNavigationMenuUseCase removeNavigationMenuUseCase;

    /**
     * GnbCommandV1Controller 생성자.
     *
     * @param mapper v1 GNB Command API 매퍼
     * @param registerNavigationMenuUseCase 네비게이션 메뉴 등록 UseCase
     * @param updateNavigationMenuUseCase 네비게이션 메뉴 수정 UseCase
     * @param removeNavigationMenuUseCase 네비게이션 메뉴 삭제 UseCase
     */
    public GnbCommandV1Controller(
            GnbCommandV1ApiMapper mapper,
            RegisterNavigationMenuUseCase registerNavigationMenuUseCase,
            UpdateNavigationMenuUseCase updateNavigationMenuUseCase,
            RemoveNavigationMenuUseCase removeNavigationMenuUseCase) {
        this.mapper = mapper;
        this.registerNavigationMenuUseCase = registerNavigationMenuUseCase;
        this.updateNavigationMenuUseCase = updateNavigationMenuUseCase;
        this.removeNavigationMenuUseCase = removeNavigationMenuUseCase;
    }

    /**
     * GNB 일괄 등록/수정/삭제 API.
     *
     * <p>toUpdateGnbs에서 gnbId가 null이면 신규 등록, gnbId가 있으면 수정합니다. deleteGnbIds에 포함된 ID는 삭제합니다.
     *
     * @param request GNB 일괄 처리 요청 DTO
     * @return 신규 등록된 GNB ID 목록
     */
    @Operation(
            summary = "GNB 일괄 등록/수정/삭제",
            description =
                    "GNB를 일괄 처리합니다. gnbId가 null이면 신규 등록, 값이 있으면 수정, deleteGnbIds에 포함된 ID는 삭제됩니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "처리 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping("/gnbs")
    public ResponseEntity<V1ApiResponse<List<Long>>> enrollGnbs(
            @RequestBody @Valid UpdateGnbV1ApiRequest request) {
        List<Long> createdIds = new ArrayList<>();

        if (request.toUpdateGnbs() != null) {
            for (CreateGnbV1ApiRequest gnb : request.toUpdateGnbs()) {
                if (gnb.gnbId() == null) {
                    RegisterNavigationMenuCommand cmd = mapper.toRegisterCommand(gnb);
                    createdIds.add(registerNavigationMenuUseCase.execute(cmd));
                } else {
                    UpdateNavigationMenuCommand cmd = mapper.toUpdateCommand(gnb);
                    updateNavigationMenuUseCase.execute(cmd);
                }
            }
        }

        if (request.deleteGnbIds() != null) {
            for (Long deleteId : request.deleteGnbIds()) {
                RemoveNavigationMenuCommand cmd = mapper.toRemoveCommand(deleteId);
                removeNavigationMenuUseCase.execute(cmd);
            }
        }

        return ResponseEntity.ok(V1ApiResponse.success(createdIds));
    }
}
