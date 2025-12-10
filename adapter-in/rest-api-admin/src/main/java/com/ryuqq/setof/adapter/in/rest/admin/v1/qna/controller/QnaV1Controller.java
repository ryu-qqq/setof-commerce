package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.command.CreateQnaAnswerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.command.UpdateQnaAnswerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.query.QnaFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response.CreateQnaAnswerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response.DetailQnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response.FetchQnaV1ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Qna Controller (Legacy)
 *
 * <p>
 * 레거시 API 호환을 위한 V1 Qna 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Qna (Legacy V1)", description = "레거시 Qna API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
public class QnaV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] QNA 조회", description = "특정 QNA를 조회합니다.")
    @GetMapping("/qna/{qnaId}")
    public ResponseEntity<ApiResponse<DetailQnaV1ApiResponse>> fetchQna(
            @PathVariable("qnaId") long qnaId) {

        throw new UnsupportedOperationException("QNA 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] QNA 목록 조회", description = "QNA 목록을 조회합니다.")
    @GetMapping("/qnas")
    public ResponseEntity<ApiResponse<PageApiResponse<FetchQnaV1ApiResponse>>> fetchQnas(
            @Validated @ModelAttribute QnaFilterV1ApiRequest filterDto) {

        throw new UnsupportedOperationException("QNA 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] QNA 답변 작성", description = "QNA에 답변을 작성합니다.")
    @PostMapping("/qna/reply")
    public ResponseEntity<ApiResponse<CreateQnaAnswerV1ApiResponse>> replyQna(
            @RequestBody CreateQnaAnswerV1ApiRequest createQnaAnswer) {

        throw new UnsupportedOperationException("QNA 답변 작성 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] QNA 답변 수정", description = "QNA 답변을 수정합니다.")
    @PutMapping("/qna/reply")
    public ResponseEntity<ApiResponse<CreateQnaAnswerV1ApiResponse>> updateReplyQna(
            @RequestBody UpdateQnaAnswerV1ApiRequest updateQnaAnswer) {

        throw new UnsupportedOperationException("QNA 답변 수정 기능은 아직 지원되지 않습니다.");
    }
}
