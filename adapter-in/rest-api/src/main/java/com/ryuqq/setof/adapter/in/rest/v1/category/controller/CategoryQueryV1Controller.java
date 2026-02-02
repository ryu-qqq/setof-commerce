package com.ryuqq.setof.adapter.in.rest.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.v1.category.CategoryV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryDisplayV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.category.mapper.CategoryV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesForDisplayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CategoryQueryV1Controller - 카테고리 조회 V1 API.
 *
 * <p>레거시 호환을 위한 V1 카테고리 조회 엔드포인트를 제공합니다.
 *
 * <p>API-CTR-001: @RestController 어노테이션 필수.
 *
 * <p>API-CTR-004: ResponseEntity&lt;V1ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "카테고리 조회 V1", description = "카테고리 조회 V1 API (레거시 호환)")
@RestController
public class CategoryQueryV1Controller {

    private final GetCategoriesForDisplayUseCase getCategoriesForDisplayUseCase;
    private final CategoryV1ApiMapper mapper;

    public CategoryQueryV1Controller(
            GetCategoriesForDisplayUseCase getCategoriesForDisplayUseCase,
            CategoryV1ApiMapper mapper) {
        this.getCategoriesForDisplayUseCase = getCategoriesForDisplayUseCase;
        this.mapper = mapper;
    }

    /**
     * 전체 카테고리 트리 조회 API.
     *
     * @return 전체 카테고리 트리
     */
    @Operation(summary = "전체 카테고리 트리 조회", description = "전체 카테고리를 트리 구조로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(CategoryV1Endpoints.CATEGORY)
    public ResponseEntity<V1ApiResponse<List<CategoryDisplayV1ApiResponse>>>
            getAllCategoriesAsTree() {
        List<CategoryDisplayResult> results = getCategoriesForDisplayUseCase.execute();
        List<CategoryDisplayV1ApiResponse> response = mapper.toListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
