package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command.CreateOrderQnaV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command.CreateProductQnaV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command.CreateQnaReplyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.mapper.QnaAdminV2ApiMapper;
import com.ryuqq.setof.application.qna.dto.command.CloseQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateOrderQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateProductQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateQnaReplyCommand;
import com.ryuqq.setof.application.qna.port.in.command.CloseQnaUseCase;
import com.ryuqq.setof.application.qna.port.in.command.CreateOrderQnaUseCase;
import com.ryuqq.setof.application.qna.port.in.command.CreateProductQnaUseCase;
import com.ryuqq.setof.application.qna.port.in.command.CreateQnaReplyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * QnA Admin Command Controller
 *
 * <p>문의 생성/수정/상태변경 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>DELETE 메서드 금지 - Soft Delete는 PATCH로 처리
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin QnA", description = "문의 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Qnas.BASE)
@Validated
public class QnaAdminCommandController {

    private final CreateProductQnaUseCase createProductQnaUseCase;
    private final CreateOrderQnaUseCase createOrderQnaUseCase;
    private final CreateQnaReplyUseCase createQnaReplyUseCase;
    private final CloseQnaUseCase closeQnaUseCase;
    private final QnaAdminV2ApiMapper mapper;

    public QnaAdminCommandController(
            CreateProductQnaUseCase createProductQnaUseCase,
            CreateOrderQnaUseCase createOrderQnaUseCase,
            CreateQnaReplyUseCase createQnaReplyUseCase,
            CloseQnaUseCase closeQnaUseCase,
            QnaAdminV2ApiMapper mapper) {
        this.createProductQnaUseCase = createProductQnaUseCase;
        this.createOrderQnaUseCase = createOrderQnaUseCase;
        this.createQnaReplyUseCase = createQnaReplyUseCase;
        this.closeQnaUseCase = closeQnaUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "상품 문의 생성", description = "새로운 상품 문의를 생성합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "생성 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Qnas.PRODUCT_PATH)
    public ResponseEntity<ApiResponse<Long>> createProductQna(
            @Valid @RequestBody CreateProductQnaV2ApiRequest request) {

        CreateProductQnaCommand command = mapper.toCreateProductQnaCommand(request);
        Long qnaId = createProductQnaUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(qnaId));
    }

    @Operation(summary = "주문 문의 생성", description = "새로운 주문 문의를 생성합니다. 이미지는 최대 3개까지 첨부할 수 있습니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "생성 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Qnas.ORDER_PATH)
    public ResponseEntity<ApiResponse<Long>> createOrderQna(
            @Valid @RequestBody CreateOrderQnaV2ApiRequest request) {

        CreateOrderQnaCommand command = mapper.toCreateOrderQnaCommand(request);
        Long qnaId = createOrderQnaUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(qnaId));
    }

    @Operation(summary = "문의 답변 생성", description = "문의에 답변을 작성합니다. 대댓글인 경우 parentReplyId를 지정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "생성 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "문의 또는 부모 답변 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Qnas.REPLIES_PATH)
    public ResponseEntity<ApiResponse<Long>> createReply(
            @Parameter(description = "문의 ID") @PathVariable Long qnaId,
            @Valid @RequestBody CreateQnaReplyV2ApiRequest request) {

        CreateQnaReplyCommand command = mapper.toCreateReplyCommand(qnaId, request);
        Long replyId = createQnaReplyUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(replyId));
    }

    @Operation(summary = "문의 종료", description = "문의를 종료합니다. 종료된 문의에는 더 이상 답변을 달 수 없습니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "종료 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "이미 종료된 문의",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "문의 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PatchMapping(ApiV2Paths.Qnas.CLOSE_PATH)
    public ResponseEntity<ApiResponse<Void>> closeQna(
            @Parameter(description = "문의 ID") @PathVariable Long qnaId) {

        CloseQnaCommand command = mapper.toCloseCommand(qnaId);
        closeQnaUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
