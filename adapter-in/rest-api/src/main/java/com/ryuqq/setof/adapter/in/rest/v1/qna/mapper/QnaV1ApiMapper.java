package com.ryuqq.setof.adapter.in.rest.v1.qna.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.CreateQnaReplyV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.CreateQnaV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.SearchMyQnasCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.SearchProductQnasV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.UpdateQnaReplyV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request.UpdateQnaV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.CreateQnaReplyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.CreateQnaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.QnaPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.QnaSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response.QnaV1ApiResponse;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaAnswerCommand;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaAnswerCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.MyQnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;
import com.ryuqq.setof.application.qna.dto.response.QnaAnswerDetailResult;
import com.ryuqq.setof.application.qna.dto.response.QnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * QnaV1ApiMapper - Q&A V1 Public API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>Request → ApplicationDTO, ApplicationResult → Response 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaV1ApiMapper {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    // ─────────────────────────────────────────────
    // Query 변환
    // ─────────────────────────────────────────────

    /**
     * 상품 Q&A 목록 검색 요청 → ProductQnaSearchParams 변환.
     *
     * <p>기본값 처리: page가 null이면 0, size가 null이면 10으로 설정.
     *
     * @param productGroupId 상품그룹 ID
     * @param viewerUserId 조회자 사용자 ID (비밀글 마스킹 판단용, 비로그인 시 null)
     * @param request 검색 요청 DTO
     * @return ProductQnaSearchParams
     */
    public ProductQnaSearchParams toProductQnaSearchParams(
            Long productGroupId,
            Long viewerUserId,
            SearchProductQnasV1ApiRequest request) {
        int page = (request.page() != null) ? request.page() : DEFAULT_PAGE;
        int size = (request.size() != null) ? request.size() : DEFAULT_SIZE;
        return ProductQnaSearchParams.of(productGroupId, viewerUserId, page, size);
    }

    /**
     * 내 Q&A 목록 검색 요청 → MyQnaSearchParams 변환.
     *
     * <p>기본값 처리: size가 null이면 10으로 설정.
     *
     * @param userId 인증된 사용자 ID
     * @param request 검색 요청 DTO
     * @return MyQnaSearchParams
     */
    public MyQnaSearchParams toMyQnaSearchParams(Long userId, SearchMyQnasCursorV1ApiRequest request) {
        int size = (request.size() != null) ? request.size() : DEFAULT_SIZE;
        return MyQnaSearchParams.of(
                userId,
                request.qnaType(),
                request.lastQnaId(),
                request.startDate(),
                request.endDate(),
                size);
    }

    /**
     * QnaPageResult → QnaPageV1ApiResponse 변환.
     *
     * @param result 상품 Q&A 페이지 결과
     * @return QnaPageV1ApiResponse
     */
    public QnaPageV1ApiResponse toPageResponse(QnaPageResult result) {
        List<QnaV1ApiResponse> content =
                result.content().stream().map(this::toQnaResponse).toList();

        return new QnaPageV1ApiResponse(
                content,
                result.pageMeta().page(),
                result.pageMeta().size(),
                result.pageMeta().totalElements(),
                result.pageMeta().totalPages());
    }

    /**
     * MyQnaSliceResult → QnaSliceV1ApiResponse 변환.
     *
     * @param result 내 Q&A 슬라이스 결과
     * @return QnaSliceV1ApiResponse
     */
    public QnaSliceV1ApiResponse toSliceResponse(MyQnaSliceResult result) {
        List<QnaV1ApiResponse> content =
                result.content().stream().map(this::toMyQnaResponse).toList();

        Long lastQnaId =
                result.content().isEmpty()
                        ? null
                        : result.content().get(result.content().size() - 1).qnaId();

        return new QnaSliceV1ApiResponse(
                content,
                result.sliceMeta().size(),
                result.sliceMeta().hasNext(),
                lastQnaId);
    }

    private QnaV1ApiResponse toQnaResponse(QnaDetailResult r) {
        QnaV1ApiResponse.QnaDetailResponse detail = new QnaV1ApiResponse.QnaDetailResponse(
                r.qnaId(),
                r.title(),
                r.content(),
                r.privateYn(),
                r.qnaStatus(),
                r.qnaType(),
                r.qnaDetailType(),
                r.userType(),
                List.of(),
                null,
                r.userName(),
                r.insertDate(),
                r.updateDate());

        Set<QnaV1ApiResponse.QnaAnswerResponse> answers = toAnswerResponseSet(r.answers());

        return new QnaV1ApiResponse(detail, answers);
    }

    private QnaV1ApiResponse toMyQnaResponse(MyQnaDetailResult r) {
        QnaV1ApiResponse.QnaTargetResponse target = null;
        if (r.productGroupId() != null) {
            QnaV1ApiResponse.BrandResponse brand = null;
            if (r.brandId() != null) {
                brand = new QnaV1ApiResponse.BrandResponse(r.brandId(), r.brandName());
            }
            target = new QnaV1ApiResponse.QnaTargetResponse(
                    r.productGroupId(),
                    r.productGroupName(),
                    r.imageUrl(),
                    brand,
                    r.paymentId(),
                    r.orderId(),
                    r.orderAmount(),
                    r.quantity(),
                    r.option());
        }

        QnaV1ApiResponse.QnaDetailResponse detail = new QnaV1ApiResponse.QnaDetailResponse(
                r.qnaId(),
                r.title(),
                r.content(),
                r.privateYn(),
                r.qnaStatus(),
                r.qnaType(),
                r.qnaDetailType(),
                r.userType(),
                List.of(),
                target,
                r.userName(),
                r.insertDate(),
                r.updateDate());

        Set<QnaV1ApiResponse.QnaAnswerResponse> answers = toAnswerResponseSet(r.answers());

        return new QnaV1ApiResponse(detail, answers);
    }

    private Set<QnaV1ApiResponse.QnaAnswerResponse> toAnswerResponseSet(
            Set<QnaAnswerDetailResult> answers) {
        if (answers == null || answers.isEmpty()) {
            return Set.of();
        }
        return answers.stream()
                .map(this::toAnswerResponse)
                .collect(Collectors.toSet());
    }

    private QnaV1ApiResponse.QnaAnswerResponse toAnswerResponse(QnaAnswerDetailResult a) {
        return new QnaV1ApiResponse.QnaAnswerResponse(
                a.qnaAnswerId(),
                a.qnaParentId(),
                a.qnaWriterType(),
                a.title(),
                a.content(),
                List.of(),
                a.insertDate(),
                a.updateDate());
    }

    // ─────────────────────────────────────────────
    // Command 변환
    // ─────────────────────────────────────────────

    /**
     * CreateQnaV1ApiRequest → RegisterQnaCommand 변환.
     *
     * <p>이미지 URL 목록은 images 필드에서 imageUrl을 추출합니다.
     *
     * @param userId 인증된 사용자 ID
     * @param request Q&A 등록 요청 DTO
     * @return RegisterQnaCommand
     */
    public RegisterQnaCommand toRegisterCommand(Long userId, CreateQnaV1ApiRequest request) {
        List<String> imageUrls =
                request.images() != null
                        ? request.images().stream()
                                .map(CreateQnaV1ApiRequest.QnaImageRequest::imageUrl)
                                .toList()
                        : List.of();

        return RegisterQnaCommand.of(
                userId,
                request.sellerId(),
                request.qnaType(),
                request.qnaDetailType(),
                request.productGroupId(),
                request.orderId(),
                request.title(),
                request.content(),
                request.secret(),
                imageUrls);
    }

    /**
     * UpdateQnaV1ApiRequest → ModifyQnaCommand 변환.
     *
     * @param qnaId 수정 대상 Q&A ID
     * @param userId 인증된 사용자 ID
     * @param request Q&A 수정 요청 DTO
     * @return ModifyQnaCommand
     */
    public ModifyQnaCommand toModifyCommand(Long qnaId, Long userId, UpdateQnaV1ApiRequest request) {
        List<String> imageUrls =
                request.images() != null
                        ? request.images().stream()
                                .map(UpdateQnaV1ApiRequest.QnaImageRequest::imageUrl)
                                .toList()
                        : List.of();

        return ModifyQnaCommand.of(
                qnaId,
                userId,
                request.title(),
                request.content(),
                request.secret(),
                imageUrls);
    }

    /**
     * CreateQnaReplyV1ApiRequest → RegisterQnaAnswerCommand 변환.
     *
     * @param qnaId 대상 Q&A ID
     * @param request Q&A 답변 등록 요청 DTO
     * @return RegisterQnaAnswerCommand
     */
    public RegisterQnaAnswerCommand toRegisterAnswerCommand(
            Long qnaId, CreateQnaReplyV1ApiRequest request) {
        return RegisterQnaAnswerCommand.of(qnaId, request.sellerId(), request.content());
    }

    /**
     * UpdateQnaReplyV1ApiRequest → ModifyQnaAnswerCommand 변환.
     *
     * @param qnaId 대상 Q&A ID
     * @param request Q&A 답변 수정 요청 DTO
     * @return ModifyQnaAnswerCommand
     */
    public ModifyQnaAnswerCommand toModifyAnswerCommand(
            Long qnaId, UpdateQnaReplyV1ApiRequest request) {
        return ModifyQnaAnswerCommand.of(qnaId, request.sellerId(), request.content());
    }

    /**
     * Long(qnaId) → CreateQnaV1ApiResponse 변환 (등록 완료 응답).
     *
     * <p>UseCase가 qnaId만 반환하므로 최소 필드로 응답을 구성합니다.
     *
     * @param qnaId 생성된 Q&A ID
     * @param request 등록 요청 DTO (응답 필드 구성에 사용)
     * @return CreateQnaV1ApiResponse
     */
    public CreateQnaV1ApiResponse toCreateQnaResponse(Long qnaId, CreateQnaV1ApiRequest request) {
        return new CreateQnaV1ApiResponse(
                qnaId,
                request.qnaType(),
                request.qnaDetailType(),
                request.title(),
                request.content(),
                request.secret(),
                "OPEN",
                request.sellerId(),
                null,
                null);
    }

    /**
     * Long(qnaAnswerId) → CreateQnaReplyV1ApiResponse 변환 (답변 등록 완료 응답).
     *
     * @param qnaAnswerId 생성된 답변 ID
     * @param qnaId 연결된 Q&A ID
     * @param request 답변 등록 요청 DTO
     * @return CreateQnaReplyV1ApiResponse
     */
    public CreateQnaReplyV1ApiResponse toCreateQnaReplyResponse(
            Long qnaAnswerId, Long qnaId, CreateQnaReplyV1ApiRequest request) {
        return new CreateQnaReplyV1ApiResponse(
                qnaAnswerId,
                qnaId,
                null,
                "CUSTOMER",
                "OPEN",
                request.title(),
                request.content(),
                null,
                null);
    }
}
