package com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.NavigationMenuAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.RegisterNavigationMenuApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.UpdateNavigationMenuApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.response.RegisterNavigationMenuApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.mapper.NavigationMenuCommandApiMapper;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.port.in.command.RegisterNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.port.in.command.RemoveNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.port.in.command.UpdateNavigationMenuUseCase;
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
 * NavigationMenuCommandController - 네비게이션 메뉴 생성/수정/삭제 API.
 *
 * <p>네비게이션 메뉴 등록, 수정, 소프트 삭제 엔드포인트를 제공합니다.
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
@Tag(name = "네비게이션 메뉴 관리", description = "네비게이션 메뉴 생성/수정/삭제 API")
@RestController
@RequestMapping(NavigationMenuAdminEndpoints.NAVIGATION_MENUS)
public class NavigationMenuCommandController {

    private final RegisterNavigationMenuUseCase registerUseCase;
    private final UpdateNavigationMenuUseCase updateUseCase;
    private final RemoveNavigationMenuUseCase removeUseCase;
    private final NavigationMenuCommandApiMapper mapper;

    /**
     * NavigationMenuCommandController 생성자.
     *
     * @param registerUseCase 네비게이션 메뉴 등록 UseCase
     * @param updateUseCase 네비게이션 메뉴 수정 UseCase
     * @param removeUseCase 네비게이션 메뉴 소프트 삭제 UseCase
     * @param mapper Command API 매퍼
     */
    public NavigationMenuCommandController(
            RegisterNavigationMenuUseCase registerUseCase,
            UpdateNavigationMenuUseCase updateUseCase,
            RemoveNavigationMenuUseCase removeUseCase,
            NavigationMenuCommandApiMapper mapper) {
        this.registerUseCase = registerUseCase;
        this.updateUseCase = updateUseCase;
        this.removeUseCase = removeUseCase;
        this.mapper = mapper;
    }

    /**
     * 네비게이션 메뉴 등록 API.
     *
     * <p>새로운 네비게이션 메뉴를 등록합니다.
     *
     * @param request 등록 요청 DTO
     * @return 생성된 네비게이션 메뉴 ID
     */
    @Operation(summary = "네비게이션 메뉴 등록", description = "새로운 네비게이션 메뉴를 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<RegisterNavigationMenuApiResponse>> register(
            @Valid @RequestBody RegisterNavigationMenuApiRequest request) {

        RegisterNavigationMenuCommand command = mapper.toCommand(request);
        Long createdId = registerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(new RegisterNavigationMenuApiResponse(createdId)));
    }

    /**
     * 네비게이션 메뉴 수정 API.
     *
     * <p>기존 네비게이션 메뉴의 정보를 수정합니다.
     *
     * @param navigationMenuId 네비게이션 메뉴 ID
     * @param request 수정 요청 DTO
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "네비게이션 메뉴 수정", description = "네비게이션 메뉴의 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "네비게이션 메뉴를 찾을 수 없음")
    })
    @PutMapping(NavigationMenuAdminEndpoints.ID)
    public ResponseEntity<Void> update(
            @Parameter(description = "네비게이션 메뉴 ID", required = true)
                    @PathVariable(NavigationMenuAdminEndpoints.PATH_NAVIGATION_MENU_ID)
                    long navigationMenuId,
            @Valid @RequestBody UpdateNavigationMenuApiRequest request) {

        UpdateNavigationMenuCommand command = mapper.toCommand(navigationMenuId, request);
        updateUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }

    /**
     * 네비게이션 메뉴 소프트 삭제 API.
     *
     * <p>네비게이션 메뉴를 논리 삭제합니다. (API-CTR-002: DELETE 금지, PATCH 사용)
     *
     * @param navigationMenuId 삭제 대상 네비게이션 메뉴 ID
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "네비게이션 메뉴 삭제", description = "네비게이션 메뉴를 논리 삭제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "네비게이션 메뉴를 찾을 수 없음")
    })
    @PatchMapping(NavigationMenuAdminEndpoints.ID + NavigationMenuAdminEndpoints.REMOVE)
    public ResponseEntity<Void> remove(
            @Parameter(description = "네비게이션 메뉴 ID", required = true)
                    @PathVariable(NavigationMenuAdminEndpoints.PATH_NAVIGATION_MENU_ID)
                    long navigationMenuId) {

        RemoveNavigationMenuCommand command = mapper.toCommand(navigationMenuId);
        removeUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }
}
