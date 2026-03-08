package com.ryuqq.setof.adapter.in.rest.v1.navigation.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.NavigationV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.dto.response.NavigationMenuV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.mapper.NavigationV1ApiMapper;
import com.ryuqq.setof.application.navigation.port.in.NavigationMenuQueryUseCase;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NavigationQueryV1Controller - 네비게이션 조회 V1 Public API.
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
@Tag(name = "네비게이션 조회 V1", description = "GNB (Global Navigation Bar) 조회 V1 Public API")
@RestController
@RequestMapping(NavigationV1Endpoints.GNBS)
public class NavigationQueryV1Controller {

    private final NavigationMenuQueryUseCase navigationMenuQueryUseCase;
    private final NavigationV1ApiMapper mapper;

    public NavigationQueryV1Controller(
            NavigationMenuQueryUseCase navigationMenuQueryUseCase, NavigationV1ApiMapper mapper) {
        this.navigationMenuQueryUseCase = navigationMenuQueryUseCase;
        this.mapper = mapper;
    }

    /**
     * GNB 목록 조회 API.
     *
     * <p>GET /api/v1/content/gnbs - 전시 중인 GNB 메뉴 목록 조회.
     *
     * @return GNB 메뉴 목록
     */
    @Operation(
            summary = "GNB 목록 조회",
            description = "전시 중인 GNB (Global Navigation Bar) 메뉴 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<List<NavigationMenuV1ApiResponse>>> getGnbs() {
        List<NavigationMenu> menus = navigationMenuQueryUseCase.fetchNavigationMenus();
        List<NavigationMenuV1ApiResponse> response = mapper.toListResponse(menus);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
