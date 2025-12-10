package com.ryuqq.setof.adapter.in.rest.v1.qna.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command.CreateQnaReplyV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command.CreateQnaV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command.UpdateQnaV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.query.QnaProductV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.CreateQnaAnswerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.CreateQnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.QnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.UpdateQnaAnswerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.UpdateQnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.query.QnaV1SearchApiRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "QnA (Legacy V1)", description = "레거시 QnA API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class QnaV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 상품 QnA 목록 조회", description = "상품별 QnA 목록을 조회합니다.")
    @GetMapping(ApiPaths.Qna.PRODUCT_LIST)
    public ResponseEntity<ApiResponse<PageApiResponse<QnaV1ApiResponse>>> getProductQnas(
            @PathVariable("productGroupId") long productGroupId, QnaProductV1SearchApiRequest request) {
        throw new UnsupportedOperationException("상품 QnA 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] QnA 질문 등록", description = "QnA 질문을 등록합니다.")
    @PostMapping(ApiPaths.Qna.CREATE)
    public ResponseEntity<ApiResponse<CreateQnaV1ApiResponse>> doQuestion(
            @AuthenticationPrincipal MemberPrincipal principal,
            @RequestBody @Validated CreateQnaV1ApiRequest request) {
        throw new UnsupportedOperationException("QnA 질문 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] QnA 질문 수정", description = "QnA 질문을 수정합니다.")
    @PutMapping(ApiPaths.Qna.UPDATE)
    public ResponseEntity<ApiResponse<UpdateQnaV1ApiResponse>> updateQuestion(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("qnaId") long qnaId,
            @RequestBody @Validated UpdateQnaV1ApiRequest request) {
        throw new UnsupportedOperationException("QnA 질문 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] QnA 답변 등록", description = "QnA 답변을 등록합니다.")
    @PostMapping(ApiPaths.Qna.REPLY)
    public ResponseEntity<ApiResponse<CreateQnaAnswerV1ApiResponse>> doReply(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("qnaId") long qnaId,
            @RequestBody @Validated CreateQnaReplyV1ApiRequest request) {
        throw new UnsupportedOperationException("QnA 답변 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] QnA 답변 수정", description = "QnA 답변을 수정합니다.")
    @PutMapping(ApiPaths.Qna.REPLY_UPDATE)
    public ResponseEntity<ApiResponse<UpdateQnaAnswerV1ApiResponse>> updateReply(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("qnaId") long qnaId,
            @PathVariable("qnaAnswerId") long qnaAnswerId,
            @RequestBody @Validated CreateQnaReplyV1ApiRequest request) {
        throw new UnsupportedOperationException("QnA 답변 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 내 QnA 목록 조회", description = "내가 작성한 QnA 목록을 조회합니다.")
    @GetMapping(ApiPaths.Qna.MY_LIST)
    public ResponseEntity<ApiResponse<QnaV1ApiResponse>> getMyQnas(
            @AuthenticationPrincipal MemberPrincipal principal,
            @ModelAttribute QnaV1SearchApiRequest request) {
        throw new UnsupportedOperationException("내 QnA 목록 조회 기능은 아직 지원되지 않습니다.");
    }

}
