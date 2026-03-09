package com.ryuqq.setof.adapter.in.rest.v1.qna.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.QnaV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.CreateQnaReplyV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.CreateQnaV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.UpdateQnaReplyV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.UpdateQnaV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.CreateQnaReplyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.CreateQnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.mapper.QnaV1ApiMapper;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaAnswerCommand;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaAnswerCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.port.in.command.ModifyQnaAnswerUseCase;
import com.ryuqq.setof.application.qna.port.in.command.ModifyQnaUseCase;
import com.ryuqq.setof.application.qna.port.in.command.RegisterQnaAnswerUseCase;
import com.ryuqq.setof.application.qna.port.in.command.RegisterQnaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * QnaCommandV1Controller - Q&A 명령 V1 Public API.
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
 *   <li>POST /api/v1/qna - Q&A 질문 등록 (인증 필요)
 *   <li>PUT /api/v1/qna/{qnaId} - Q&A 질문 수정 (인증 필요)
 *   <li>POST /api/v1/qna/{qnaId}/reply - Q&A 답변 등록 (인증 필요)
 *   <li>PUT /api/v1/qna/{qnaId}/reply/{qnaAnswerId} - Q&A 답변 수정 (인증 필요)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "Q&A 명령", description = "Q&A 질문/답변 등록 및 수정 V1 Public API (인증 필요)")
@RestController
public class QnaCommandV1Controller {

    private final RegisterQnaUseCase registerQnaUseCase;
    private final ModifyQnaUseCase modifyQnaUseCase;
    private final RegisterQnaAnswerUseCase registerQnaAnswerUseCase;
    private final ModifyQnaAnswerUseCase modifyQnaAnswerUseCase;
    private final QnaV1ApiMapper mapper;

    public QnaCommandV1Controller(
            RegisterQnaUseCase registerQnaUseCase,
            ModifyQnaUseCase modifyQnaUseCase,
            RegisterQnaAnswerUseCase registerQnaAnswerUseCase,
            ModifyQnaAnswerUseCase modifyQnaAnswerUseCase,
            QnaV1ApiMapper mapper) {
        this.registerQnaUseCase = registerQnaUseCase;
        this.modifyQnaUseCase = modifyQnaUseCase;
        this.registerQnaAnswerUseCase = registerQnaAnswerUseCase;
        this.modifyQnaAnswerUseCase = modifyQnaAnswerUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Q&A 질문 등록",
            description = "인증된 사용자가 Q&A 질문을 등록합니다. PRODUCT/ORDER 유형 모두 지원합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(QnaV1Endpoints.QNA)
    public ResponseEntity<V1ApiResponse<CreateQnaV1ApiResponse>> registerQna(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Valid @RequestBody CreateQnaV1ApiRequest request) {

        RegisterQnaCommand command = mapper.toRegisterCommand(userId, request);
        Long qnaId = registerQnaUseCase.execute(command);
        CreateQnaV1ApiResponse response = mapper.toCreateQnaResponse(qnaId, request);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(
            summary = "Q&A 질문 수정",
            description = "인증된 사용자가 자신의 Q&A 질문을 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "수정 권한 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Q&A를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping(QnaV1Endpoints.QNA_BY_ID)
    public ResponseEntity<V1ApiResponse<Void>> modifyQna(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Parameter(description = "수정할 Q&A ID", required = true)
                    @PathVariable(QnaV1Endpoints.PATH_QNA_ID)
                    Long qnaId,
            @Valid @RequestBody UpdateQnaV1ApiRequest request) {

        ModifyQnaCommand command = mapper.toModifyCommand(qnaId, userId, request);
        modifyQnaUseCase.execute(command);

        return ResponseEntity.ok(V1ApiResponse.success(null));
    }

    @Operation(
            summary = "Q&A 답변 등록",
            description = "인증된 사용자가 Q&A에 답변을 등록합니다. 답변 등록 시 Q&A 상태가 ANSWERED로 전환됩니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Q&A를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(QnaV1Endpoints.QNA_REPLY)
    public ResponseEntity<V1ApiResponse<CreateQnaReplyV1ApiResponse>> registerQnaAnswer(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Parameter(description = "답변 대상 Q&A ID", required = true)
                    @PathVariable(QnaV1Endpoints.PATH_QNA_ID)
                    Long qnaId,
            @Valid @RequestBody CreateQnaReplyV1ApiRequest request) {

        RegisterQnaAnswerCommand command = mapper.toRegisterAnswerCommand(qnaId, request);
        Long qnaAnswerId = registerQnaAnswerUseCase.execute(command);
        CreateQnaReplyV1ApiResponse response =
                mapper.toCreateQnaReplyResponse(qnaAnswerId, qnaId, request);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(
            summary = "Q&A 답변 수정",
            description = "인증된 사용자가 Q&A 답변을 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "수정 권한 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "답변을 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping(QnaV1Endpoints.QNA_REPLY_BY_ID)
    public ResponseEntity<V1ApiResponse<Void>> modifyQnaAnswer(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Parameter(description = "수정할 답변의 Q&A ID", required = true)
                    @PathVariable(QnaV1Endpoints.PATH_QNA_ID)
                    Long qnaId,
            @Parameter(description = "수정할 답변 ID", required = true)
                    @PathVariable(QnaV1Endpoints.PATH_QNA_ANSWER_ID)
                    Long qnaAnswerId,
            @Valid @RequestBody UpdateQnaReplyV1ApiRequest request) {

        ModifyQnaAnswerCommand command =
                mapper.toModifyAnswerCommand(qnaId, qnaAnswerId, request);
        modifyQnaAnswerUseCase.execute(command);

        return ResponseEntity.ok(V1ApiResponse.success(null));
    }
}
