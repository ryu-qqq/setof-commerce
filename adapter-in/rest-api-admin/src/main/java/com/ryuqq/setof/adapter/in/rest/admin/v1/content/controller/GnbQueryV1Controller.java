package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchGnbsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.GnbV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.GnbQueryV1ApiMapper;
import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.application.navigation.port.in.query.SearchNavigationMenusUseCase;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GnbQueryV1Controller - GNB 조회 v1 API.
 *
 * <p>레거시 GNB 조회 API와 동일한 URL, HTTP 메서드, 요청/응답 구조를 유지합니다.
 *
 * <p>레거시 호환 URL:
 *
 * <ul>
 *   <li>GET /api/v1/content/gnbs — GNB 목록 조회
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "GNB 조회 v1", description = "GNB 목록 조회 v1 API (레거시 호환)")
@RestController
@RequestMapping("/api/v1/content")
public class GnbQueryV1Controller {

    private final SearchNavigationMenusUseCase searchNavigationMenusUseCase;
    private final GnbQueryV1ApiMapper queryMapper;

    /**
     * GnbQueryV1Controller 생성자.
     *
     * @param searchNavigationMenusUseCase 네비게이션 메뉴 검색 UseCase
     * @param queryMapper GNB Query API 매퍼
     */
    public GnbQueryV1Controller(
            SearchNavigationMenusUseCase searchNavigationMenusUseCase,
            GnbQueryV1ApiMapper queryMapper) {
        this.searchNavigationMenusUseCase = searchNavigationMenusUseCase;
        this.queryMapper = queryMapper;
    }

    /**
     * GNB 목록 조회 API.
     *
     * <p>전시 기간으로 필터링하여 GNB 메뉴 목록을 조회합니다.
     *
     * @param filter 검색 필터 DTO (시작일, 종료일, 검색 키워드)
     * @return GNB 목록
     */
    @Operation(summary = "GNB 목록 조회", description = "GNB 메뉴 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/gnbs")
    public ResponseEntity<V1ApiResponse<List<GnbV1ApiResponse>>> getGnbs(
            @ModelAttribute SearchGnbsV1ApiRequest filter) {

        NavigationMenuSearchParams params = queryMapper.toSearchParams(filter);
        List<NavigationMenu> menus = searchNavigationMenusUseCase.execute(params);

        List<GnbV1ApiResponse> responses = menus.stream().map(queryMapper::toGnbResponse).toList();

        return ResponseEntity.ok(V1ApiResponse.success(responses));
    }
}
