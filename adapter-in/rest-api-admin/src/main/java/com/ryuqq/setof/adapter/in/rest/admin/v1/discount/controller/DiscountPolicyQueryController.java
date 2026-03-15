package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request.DiscountPolicySearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request.DiscountTargetSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request.DiscountUseHistorySearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountTargetV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountUseHistoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper.DiscountPolicyCommandApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper.DiscountPolicyQueryApiMapper;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyPageResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyDetailUseCase;
import com.ryuqq.setof.application.discount.port.in.query.SearchDiscountPoliciesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DiscountPolicyQueryController - 할인 정책 조회 API (v1 레거시 호환).
 *
 * <p>레거시 URL 엔드포인트를 그대로 유지하면서 새 Application UseCase를 호출합니다. 대상 조회 / 이력 조회 / 사용 이력 조회는 Application에
 * 해당 UseCase가 아직 없으므로 엔드포인트만 선언하고 UnsupportedOperationException을 던집니다.
 *
 * <p>API-CTR-001: Controller는 @RestController로 정의.
 *
 * <p>API-CTR-004: ResponseEntity&lt;ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "할인 정책 조회 (v1)", description = "레거시 호환 할인 정책 조회 API")
@RestController
@RequestMapping("/api/v1")
public class DiscountPolicyQueryController {

    private final GetDiscountPolicyDetailUseCase getDetailUseCase;
    private final SearchDiscountPoliciesUseCase searchUseCase;
    private final DiscountPolicyCommandApiMapper commandMapper;
    private final DiscountPolicyQueryApiMapper queryMapper;

    /**
     * DiscountPolicyQueryController 생성자.
     *
     * @param getDetailUseCase 할인 정책 단건 조회 UseCase
     * @param searchUseCase 할인 정책 목록 검색 UseCase
     * @param commandMapper Command API 매퍼 (toResponse() 사용)
     * @param queryMapper Query API 매퍼
     */
    public DiscountPolicyQueryController(
            GetDiscountPolicyDetailUseCase getDetailUseCase,
            SearchDiscountPoliciesUseCase searchUseCase,
            DiscountPolicyCommandApiMapper commandMapper,
            DiscountPolicyQueryApiMapper queryMapper) {
        this.getDetailUseCase = getDetailUseCase;
        this.searchUseCase = searchUseCase;
        this.commandMapper = commandMapper;
        this.queryMapper = queryMapper;
    }

    /**
     * 할인 정책 단건 조회 API.
     *
     * <p>ID로 특정 할인 정책의 상세 정보를 조회합니다.
     *
     * @param discountPolicyId 조회할 할인 정책 ID
     * @return 할인 정책 상세 정보 (200 OK)
     */
    @Operation(summary = "할인 정책 단건 조회", description = "ID로 특정 할인 정책의 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "할인 정책을 찾을 수 없음")
    })
    @GetMapping("/discount/{discountPolicyId}")
    public ResponseEntity<ApiResponse<DiscountPolicyV1ApiResponse>> getDetail(
            @Parameter(description = "할인 정책 ID", required = true) @PathVariable
                    long discountPolicyId) {

        DiscountPolicyResult result = getDetailUseCase.execute(discountPolicyId);
        DiscountPolicyV1ApiResponse response = commandMapper.toResponse(result);

        return ResponseEntity.ok(ApiResponse.of(response));
    }

    /**
     * 할인 정책 목록 조회 API.
     *
     * <p>필터 조건과 페이징 정보로 할인 정책 목록을 조회합니다. 레거시의 CustomPageable 응답 형식을
     * DiscountPolicyPageV1ApiResponse로 간소화하여 반환합니다.
     *
     * @param filter 검색 필터 및 페이징 요청 DTO
     * @return 할인 정책 페이지 결과 (200 OK)
     */
    @Operation(summary = "할인 정책 목록 조회", description = "필터 조건과 페이징 정보로 할인 정책 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @GetMapping("/discounts")
    public ResponseEntity<ApiResponse<DiscountPolicyPageV1ApiResponse>> search(
            @ModelAttribute DiscountPolicySearchV1ApiRequest filter) {

        DiscountPolicySearchParams params = queryMapper.toSearchParams(filter.withDefaults());
        DiscountPolicyPageResult pageResult = searchUseCase.execute(params);

        List<DiscountPolicyV1ApiResponse> items =
                pageResult.items().stream().map(commandMapper::toResponse).toList();

        DiscountPolicyPageV1ApiResponse response =
                new DiscountPolicyPageV1ApiResponse(
                        items, pageResult.totalCount(), pageResult.page(), pageResult.size());

        return ResponseEntity.ok(ApiResponse.of(response));
    }

    /**
     * 할인 대상 조회 API.
     *
     * <p>TODO: Application에 해당 UseCase가 아직 없습니다. UseCase 구현 후 연동 필요.
     *
     * @param discountPolicyId 할인 정책 ID
     * @param filter 대상 검색 필터 DTO (issueType, page, size)
     * @return 미구현 (UnsupportedOperationException)
     */
    @Operation(summary = "할인 대상 조회 (미구현)", description = "TODO: Application UseCase 구현 후 연동 예정.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "미구현")
    })
    @GetMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<List<DiscountTargetV1ApiResponse>>> getTargets(
            @Parameter(description = "할인 정책 ID", required = true) @PathVariable
                    long discountPolicyId,
            @ModelAttribute DiscountTargetSearchV1ApiRequest filter) {

        // TODO: GetDiscountTargetsByPolicyUseCase 구현 후 연동
        throw new UnsupportedOperationException(
                "할인 대상 조회 UseCase가 아직 구현되지 않았습니다. discountPolicyId=" + discountPolicyId);
    }

    /**
     * 할인 이력 조회 API.
     *
     * <p>TODO: Application에 해당 UseCase가 아직 없습니다. UseCase 구현 후 연동 필요.
     *
     * @param filter 이력 검색 필터 DTO
     * @return 미구현 (UnsupportedOperationException)
     */
    @Operation(summary = "할인 이력 조회 (미구현)", description = "TODO: Application UseCase 구현 후 연동 예정.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "미구현")
    })
    @GetMapping("/discounts/history")
    public ResponseEntity<ApiResponse<List<Object>>> getHistory(
            @ModelAttribute DiscountPolicySearchV1ApiRequest filter) {

        // TODO: SearchDiscountHistoryUseCase 구현 후 연동
        throw new UnsupportedOperationException("할인 이력 조회 UseCase가 아직 구현되지 않았습니다.");
    }

    /**
     * 할인 사용 이력 조회 API.
     *
     * <p>TODO: Application에 해당 UseCase가 아직 없습니다. UseCase 구현 후 연동 필요.
     *
     * @param discountPolicyId 할인 정책 ID
     * @param filter 사용 이력 검색 필터 DTO
     * @return 미구현 (UnsupportedOperationException)
     */
    @Operation(summary = "할인 사용 이력 조회 (미구현)", description = "TODO: Application UseCase 구현 후 연동 예정.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "미구현")
    })
    @GetMapping("/discount/history/{discountPolicyId}/use")
    public ResponseEntity<ApiResponse<List<DiscountUseHistoryV1ApiResponse>>> getUseHistory(
            @Parameter(description = "할인 정책 ID", required = true) @PathVariable
                    long discountPolicyId,
            @ModelAttribute DiscountUseHistorySearchV1ApiRequest filter) {

        // TODO: GetDiscountUseHistoryUseCase 구현 후 연동
        throw new UnsupportedOperationException(
                "할인 사용 이력 조회 UseCase가 아직 구현되지 않았습니다. discountPolicyId=" + discountPolicyId);
    }

    /**
     * DiscountPolicyPageV1ApiResponse - 할인 정책 목록 페이지 응답 DTO.
     *
     * <p>레거시의 CustomPageable&lt;DiscountPolicyResponseDto&gt; 를 대체하는 간소화 record입니다.
     *
     * @param items 현재 페이지의 할인 정책 응답 목록
     * @param totalCount 전체 항목 수
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     */
    public record DiscountPolicyPageV1ApiResponse(
            List<DiscountPolicyV1ApiResponse> items, long totalCount, int page, int size) {}
}
