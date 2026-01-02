package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.query.QnaSearchV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response.QnaPageV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response.QnaReplyV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response.QnaSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response.QnaV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.mapper.QnaAdminV2ApiMapper;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.qna.dto.query.QnaSearchQuery;
import com.ryuqq.setof.application.qna.dto.response.QnaReplyResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaSummaryResponse;
import com.ryuqq.setof.application.qna.port.in.query.GetQnaRepliesUseCase;
import com.ryuqq.setof.application.qna.port.in.query.GetQnaUseCase;
import com.ryuqq.setof.application.qna.port.in.query.GetQnasUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * QnA Admin Query Controller
 *
 * <p>문의 조회 API 엔드포인트 (CQRS Query 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>GET 메서드만 포함
 *   <li>Query DTO는 @ModelAttribute로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin QnA", description = "문의 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Qnas.BASE)
@Validated
public class QnaAdminQueryController {

    private final GetQnaUseCase getQnaUseCase;
    private final GetQnasUseCase getQnasUseCase;
    private final GetQnaRepliesUseCase getQnaRepliesUseCase;
    private final QnaAdminV2ApiMapper mapper;

    public QnaAdminQueryController(
            GetQnaUseCase getQnaUseCase,
            GetQnasUseCase getQnasUseCase,
            GetQnaRepliesUseCase getQnaRepliesUseCase,
            QnaAdminV2ApiMapper mapper) {
        this.getQnaUseCase = getQnaUseCase;
        this.getQnasUseCase = getQnasUseCase;
        this.getQnaRepliesUseCase = getQnaRepliesUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "문의 목록 조회", description = "검색 조건에 맞는 문의 목록을 페이징하여 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping
    public ResponseEntity<ApiResponse<QnaPageV2ApiResponse>> searchQnas(
            @Valid @ModelAttribute QnaSearchV2ApiRequest request) {

        QnaSearchQuery query = mapper.toSearchQuery(request);
        PageResponse<QnaSummaryResponse> pageResponse = getQnasUseCase.execute(query);

        List<QnaSummaryV2ApiResponse> apiResponses =
                pageResponse.content().stream().map(QnaSummaryV2ApiResponse::from).toList();

        QnaPageV2ApiResponse response =
                QnaPageV2ApiResponse.of(
                        apiResponses,
                        pageResponse.page(),
                        pageResponse.size(),
                        pageResponse.totalElements());

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    @Operation(summary = "문의 상세 조회", description = "문의 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "문의 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping(ApiV2Paths.Qnas.ID_PATH)
    public ResponseEntity<ApiResponse<QnaV2ApiResponse>> getQna(
            @Parameter(description = "문의 ID") @PathVariable Long qnaId) {

        QnaResponse response = getQnaUseCase.execute(qnaId);
        QnaV2ApiResponse apiResponse = QnaV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    @Operation(summary = "문의 답변 목록 조회", description = "문의에 달린 답변 목록을 Materialized Path 순서로 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "문의 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping(ApiV2Paths.Qnas.REPLIES_PATH)
    public ResponseEntity<ApiResponse<List<QnaReplyV2ApiResponse>>> getQnaReplies(
            @Parameter(description = "문의 ID") @PathVariable Long qnaId) {

        List<QnaReplyResponse> responses = getQnaRepliesUseCase.execute(qnaId);
        List<QnaReplyV2ApiResponse> apiResponses =
                responses.stream().map(QnaReplyV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }
}
