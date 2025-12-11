package com.ryuqq.setof.adapter.in.rest.v2.category.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.category.dto.response.CategoryPathV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.category.dto.response.CategoryTreeV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.category.dto.response.CategoryV2ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryPathUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryTreeUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Category V2 Controller
 *
 * <p>카테고리 정보 조회 API 엔드포인트
 *
 * <p>카테고리 정보는 MarketPlace에서 배치로 동기화되므로 조회만 지원합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Category", description = "카테고리 정보 조회 API")
@RestController
@RequestMapping(ApiV2Paths.Categories.BASE)
public class CategoryV2Controller {

    private final GetCategoryUseCase getCategoryUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final GetCategoryTreeUseCase getCategoryTreeUseCase;
    private final GetCategoryPathUseCase getCategoryPathUseCase;

    public CategoryV2Controller(
            GetCategoryUseCase getCategoryUseCase,
            GetCategoriesUseCase getCategoriesUseCase,
            GetCategoryTreeUseCase getCategoryTreeUseCase,
            GetCategoryPathUseCase getCategoryPathUseCase) {
        this.getCategoryUseCase = getCategoryUseCase;
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.getCategoryTreeUseCase = getCategoryTreeUseCase;
        this.getCategoryPathUseCase = getCategoryPathUseCase;
    }

    /**
     * 카테고리 단건 조회
     *
     * <p>카테고리 ID로 상세 정보를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 상세 정보
     */
    @Operation(summary = "카테고리 단건 조회", description = "카테고리 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "카테고리를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Categories.ID_PATH)
    public ResponseEntity<ApiResponse<CategoryV2ApiResponse>> getCategory(
            @Parameter(description = "카테고리 ID", example = "1") @PathVariable Long categoryId) {

        CategoryResponse categoryResponse = getCategoryUseCase.getCategory(categoryId);

        CategoryV2ApiResponse response = CategoryV2ApiResponse.from(categoryResponse);

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    /**
     * 하위 카테고리 목록 조회
     *
     * <p>부모 카테고리 ID로 하위 카테고리 목록을 조회합니다. parentId가 없으면 최상위 카테고리 목록을 반환합니다.
     *
     * @param parentId 부모 카테고리 ID (optional)
     * @return 하위 카테고리 목록
     */
    @Operation(
            summary = "하위 카테고리 목록 조회",
            description = "부모 카테고리 ID로 하위 카테고리 목록을 조회합니다. parentId가 없으면 최상위 카테고리 목록을 반환합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryV2ApiResponse>>> getCategories(
            @Parameter(description = "부모 카테고리 ID (없으면 최상위)") @RequestParam(required = false)
                    Long parentId) {

        List<CategoryResponse> categories = getCategoriesUseCase.getCategories(parentId);

        List<CategoryV2ApiResponse> response =
                categories.stream().map(CategoryV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    /**
     * 카테고리 트리 조회
     *
     * <p>전체 카테고리를 트리 구조로 조회합니다.
     *
     * @return 카테고리 트리
     */
    @Operation(summary = "카테고리 트리 조회", description = "전체 카테고리를 트리 구조로 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Categories.TREE_PATH)
    public ResponseEntity<ApiResponse<List<CategoryTreeV2ApiResponse>>> getCategoryTree() {

        List<CategoryTreeResponse> treeResponse = getCategoryTreeUseCase.getCategoryTree();

        List<CategoryTreeV2ApiResponse> response =
                treeResponse.stream().map(CategoryTreeV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    /**
     * 카테고리 경로 조회 (Breadcrumb)
     *
     * <p>카테고리 ID로 최상위부터 현재까지의 경로를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 경로 (Breadcrumb)
     */
    @Operation(summary = "카테고리 경로 조회 (Breadcrumb)", description = "카테고리 ID로 최상위부터 현재까지의 경로를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "카테고리를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/{categoryId}/path")
    public ResponseEntity<ApiResponse<CategoryPathV2ApiResponse>> getCategoryPath(
            @Parameter(description = "카테고리 ID", example = "10") @PathVariable Long categoryId) {

        CategoryPathResponse pathResponse = getCategoryPathUseCase.getCategoryPath(categoryId);

        CategoryPathV2ApiResponse response = CategoryPathV2ApiResponse.from(pathResponse);

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }
}
