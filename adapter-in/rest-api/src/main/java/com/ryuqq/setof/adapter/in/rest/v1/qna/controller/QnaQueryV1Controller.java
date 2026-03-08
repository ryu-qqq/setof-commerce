package com.ryuqq.setof.adapter.in.rest.v1.qna.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.QnaV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.SearchMyQnasCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.SearchProductQnasV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.QnaPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.QnaSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.mapper.QnaV1ApiMapper;
import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;
import com.ryuqq.setof.application.qna.port.in.query.GetMyQnasUseCase;
import com.ryuqq.setof.application.qna.port.in.query.GetProductQnasUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * QnaQueryV1Controller - Q&A 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>엔드포인트 매핑:
 *
 * <ul>
 *   <li>GET /api/v1/qna/product/{productGroupId} - 상품 Q&A 목록 조회 (인증 불필요)
 *   <li>GET /api/v1/qna/my-page - 내 Q&A 목록 조회 (인증 필요)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "Q&A 조회", description = "Q&A 조회 V1 Public API")
@RestController
public class QnaQueryV1Controller {

    private final GetProductQnasUseCase getProductQnasUseCase;
    private final GetMyQnasUseCase getMyQnasUseCase;
    private final QnaV1ApiMapper mapper;

    public QnaQueryV1Controller(
            GetProductQnasUseCase getProductQnasUseCase,
            GetMyQnasUseCase getMyQnasUseCase,
            QnaV1ApiMapper mapper) {
        this.getProductQnasUseCase = getProductQnasUseCase;
        this.getMyQnasUseCase = getMyQnasUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "상품 Q&A 목록 조회",
            description = "특정 상품그룹의 Q&A 목록을 오프셋 페이지네이션으로 조회합니다. 비밀글 마스킹 적용. 인증 불필요.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(QnaV1Endpoints.QNA_PRODUCT + QnaV1Endpoints.PRODUCT_GROUP_ID)
    public ResponseEntity<V1ApiResponse<QnaPageV1ApiResponse>> fetchProductQnas(
            @Parameter(description = "상품그룹 ID", required = true)
                    @PathVariable(QnaV1Endpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId,
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Valid @ModelAttribute SearchProductQnasV1ApiRequest request) {

        ProductQnaSearchParams params =
                mapper.toProductQnaSearchParams(productGroupId, userId, request);
        QnaPageResult result = getProductQnasUseCase.execute(params);
        QnaPageV1ApiResponse response = mapper.toPageResponse(result);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(
            summary = "내 Q&A 목록 조회",
            description = "인증된 사용자의 Q&A 목록을 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(QnaV1Endpoints.QNA_MY_PAGE)
    public ResponseEntity<V1ApiResponse<QnaSliceV1ApiResponse>> fetchMyQnas(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Valid @ModelAttribute SearchMyQnasCursorV1ApiRequest request) {

        MyQnaSearchParams params = mapper.toMyQnaSearchParams(userId, request);
        MyQnaSliceResult result = getMyQnasUseCase.execute(params);
        QnaSliceV1ApiResponse response = mapper.toSliceResponse(result);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
