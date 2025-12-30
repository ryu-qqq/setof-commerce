package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.auth.context.SecurityContextHolder;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1PageResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.command.CreateQnaAnswerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.command.UpdateQnaAnswerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.query.QnaFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response.AnswerQnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response.CreateQnaAnswerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response.DetailQnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response.FetchQnaV1ApiResponse;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.qna.dto.command.CreateQnaReplyCommand;
import com.ryuqq.setof.application.qna.dto.command.UpdateQnaReplyCommand;
import com.ryuqq.setof.application.qna.dto.query.QnaSearchQuery;
import com.ryuqq.setof.application.qna.dto.response.QnaImageResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaReplyResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaSummaryResponse;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * QnA Admin V1 API Mapper
 *
 * <p>V1 Legacy API DTO ↔ Application Command/Response 변환
 *
 * <p>Legacy API 호환성을 위해 V2 UseCase를 재사용하면서 V1 응답 형식으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Component
@Deprecated
public class QnaAdminV1ApiMapper {

    private static final String WRITER_TYPE_ADMIN = "ADMIN";
    private static final String DEFAULT_ADMIN_NAME = "관리자";
    private static final String YN_YES = "Y";
    private static final String YN_NO = "N";

    /**
     * V1 필터 요청 → QnaSearchQuery 변환
     *
     * @param filter V1 QNA 필터 요청
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return QnaSearchQuery
     */
    public QnaSearchQuery toSearchQuery(QnaFilterV1ApiRequest filter, int page, int size) {
        return new QnaSearchQuery(
                filter.qnaType(),
                filter.qnaId(), // targetId로 사용 (레거시 호환)
                filter.qnaStatus(),
                filter.searchKeyword(),
                null, // sortBy - 기본값 사용
                null, // sortDirection - 기본값 사용
                page,
                size);
    }

    /**
     * V1 답변 생성 요청 → CreateQnaReplyCommand 변환
     *
     * @param request V1 답변 생성 요청
     * @return CreateQnaReplyCommand
     */
    public CreateQnaReplyCommand toCreateReplyCommand(CreateQnaAnswerV1ApiRequest request) {
        String writerId = SecurityContextHolder.getCurrentUserId();

        return new CreateQnaReplyCommand(
                request.qnaId(),
                null, // parentReplyId - V1에서는 대댓글 미지원
                writerId,
                WRITER_TYPE_ADMIN,
                DEFAULT_ADMIN_NAME,
                request.qnaContents().content());
    }

    /**
     * V1 답변 수정 요청 → UpdateQnaReplyCommand 변환
     *
     * @param request V1 답변 수정 요청
     * @return UpdateQnaReplyCommand
     */
    public UpdateQnaReplyCommand toUpdateReplyCommand(UpdateQnaAnswerV1ApiRequest request) {
        return new UpdateQnaReplyCommand(
                request.qnaId(), request.qnaAnswerId(), request.qnaContents().content());
    }

    /**
     * QnaResponse + List<QnaReplyResponse> → DetailQnaV1ApiResponse 변환
     *
     * @param qnaResponse Application QnA 응답
     * @param replies Application 답변 목록 응답
     * @return V1 QNA 상세 응답
     */
    public DetailQnaV1ApiResponse toDetailResponse(
            QnaResponse qnaResponse, List<QnaReplyResponse> replies) {
        FetchQnaV1ApiResponse qna = toFetchQnaResponse(qnaResponse);
        Set<AnswerQnaV1ApiResponse> answerQnas = toAnswerQnaResponses(replies);
        return new DetailQnaV1ApiResponse(qna, answerQnas);
    }

    /**
     * QnaResponse → FetchQnaV1ApiResponse 변환
     *
     * @param response Application QnA 응답
     * @return V1 QNA 목록 응답
     */
    public FetchQnaV1ApiResponse toFetchQnaResponse(QnaResponse response) {
        return new FetchQnaV1ApiResponse(
                response.id(),
                new FetchQnaV1ApiResponse.QnaContentsV1ApiResponse(
                        response.title(), response.content()),
                response.isSecret() ? YN_YES : YN_NO,
                mapStatus(response.status()),
                response.type(),
                response.detailType(),
                null, // sellerName - V2에 없음
                new FetchQnaV1ApiResponse.UserInfoQnaV1ApiResponse(
                        response.writerType(),
                        null, // userId - Long 변환 불가
                        response.writerName(),
                        null, // phoneNumber - V2에 없음
                        null, // email - V2에 없음
                        null), // gender - V2에 없음
                new FetchQnaV1ApiResponse.QnaTargetV1ApiResponse(response.targetId()),
                response.createdAt(),
                response.createdAt(), // updateDate - V2에 없음
                toQnaImageV1Responses(response.images()));
    }

    /**
     * QnaSummaryResponse → FetchQnaV1ApiResponse 변환 (목록 조회용)
     *
     * @param response Application QnA 요약 응답
     * @return V1 QNA 목록 응답
     */
    public FetchQnaV1ApiResponse toFetchQnaSummaryResponse(QnaSummaryResponse response) {
        return new FetchQnaV1ApiResponse(
                response.id(),
                new FetchQnaV1ApiResponse.QnaContentsV1ApiResponse(response.title(), null),
                response.isSecret() ? YN_YES : YN_NO,
                mapStatus(response.status()),
                response.type(),
                response.detailType(),
                null, // sellerName - V2에 없음
                new FetchQnaV1ApiResponse.UserInfoQnaV1ApiResponse(
                        null, // userType - V2 Summary에 없음
                        null, // userId
                        response.writerName(),
                        null, // phoneNumber
                        null, // email
                        null), // gender
                null, // qnaTarget - V2 Summary에 없음
                response.createdAt(),
                null, // updateDate
                Collections.emptyList());
    }

    /**
     * List<QnaReplyResponse> → Set<AnswerQnaV1ApiResponse> 변환
     *
     * @param replies Application 답변 목록 응답
     * @return V1 답변 QNA Set 응답
     */
    public Set<AnswerQnaV1ApiResponse> toAnswerQnaResponses(List<QnaReplyResponse> replies) {
        if (replies == null || replies.isEmpty()) {
            return Collections.emptySet();
        }
        Set<AnswerQnaV1ApiResponse> result = new LinkedHashSet<>();
        for (QnaReplyResponse reply : replies) {
            result.add(toAnswerQnaResponse(reply));
        }
        return result;
    }

    /**
     * QnaReplyResponse → AnswerQnaV1ApiResponse 변환
     *
     * @param response Application 답변 응답
     * @return V1 답변 QNA 응답
     */
    public AnswerQnaV1ApiResponse toAnswerQnaResponse(QnaReplyResponse response) {
        return new AnswerQnaV1ApiResponse(
                response.id(),
                response.parentReplyId() != null ? response.parentReplyId() : 0L,
                response.writerType(),
                new AnswerQnaV1ApiResponse.QnaContentsV1ApiResponse(
                        null, // title - V2 Reply에 없음
                        response.content()),
                Collections.emptyList(), // qnaImages - V2 Reply에 없음
                response.writerName(), // insertOperator
                response.writerName(), // updateOperator
                response.createdAt(),
                response.createdAt()); // updateDate
    }

    /**
     * 답변 생성 결과 → CreateQnaAnswerV1ApiResponse 변환
     *
     * @param qnaId QNA ID
     * @param replyId 생성된 답변 ID
     * @param qnaType QNA 타입
     * @return V1 답변 생성 응답
     */
    public CreateQnaAnswerV1ApiResponse toCreateAnswerResponse(
            Long qnaId, Long replyId, String qnaType) {
        return new CreateQnaAnswerV1ApiResponse(
                qnaId,
                replyId,
                qnaType,
                "ANSWERED", // 답변 작성 후 상태
                Collections.emptyList());
    }

    /**
     * PageResponse → V1PageResponse 변환
     *
     * @param pageResponse Application 페이지 응답
     * @return V1 페이지 응답
     */
    public V1PageResponse<FetchQnaV1ApiResponse> toPageResponse(
            PageResponse<QnaSummaryResponse> pageResponse) {
        return V1PageResponse.from(pageResponse, this::toFetchQnaSummaryResponse);
    }

    /**
     * List<QnaImageResponse> → List<FetchQnaV1ApiResponse.QnaImageV1ApiResponse> 변환
     */
    private List<FetchQnaV1ApiResponse.QnaImageV1ApiResponse> toQnaImageV1Responses(
            List<QnaImageResponse> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        return images.stream()
                .map(
                        img ->
                                new FetchQnaV1ApiResponse.QnaImageV1ApiResponse(
                                        "QUESTION", // qnaIssueType
                                        null, // imageId - V2 QnaImageResponse에 id 필드 없음
                                        null, // qnaId
                                        null, // qnaAnswerId
                                        img.imageUrl(),
                                        img.displayOrder()))
                .toList();
    }

    /**
     * V2 status → V1 status 매핑
     */
    private String mapStatus(String v2Status) {
        if ("OPEN".equals(v2Status)) {
            return "PENDING";
        } else if ("CLOSED".equals(v2Status)) {
            return "COMPLETED";
        }
        return v2Status;
    }
}
