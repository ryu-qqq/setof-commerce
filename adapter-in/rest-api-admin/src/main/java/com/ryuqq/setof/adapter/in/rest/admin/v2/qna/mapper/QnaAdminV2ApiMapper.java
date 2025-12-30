package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.auth.context.SecurityContextHolder;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command.CreateOrderQnaV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command.CreateProductQnaV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command.CreateQnaReplyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.query.QnaSearchV2ApiRequest;
import com.ryuqq.setof.application.qna.dto.command.CloseQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateOrderQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateProductQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateQnaReplyCommand;
import com.ryuqq.setof.application.qna.dto.query.QnaSearchQuery;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * QnA Admin V2 API Mapper
 *
 * <p>QnA 관리 API DTO ↔ Application Command/Query 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class QnaAdminV2ApiMapper {

    private static final String WRITER_TYPE_CUSTOMER = "CUSTOMER";
    private static final String WRITER_TYPE_ADMIN = "ADMIN";
    private static final String DEFAULT_ADMIN_NAME = "관리자";

    /**
     * 상품 문의 생성 요청을 Command로 변환
     *
     * <p>고객 대신 문의를 생성하는 경우 사용
     *
     * @param request API 요청
     * @return Application Command
     */
    public CreateProductQnaCommand toCreateProductQnaCommand(CreateProductQnaV2ApiRequest request) {
        String writerId = SecurityContextHolder.getCurrentUserId();

        return new CreateProductQnaCommand(
                request.productGroupId(),
                writerId,
                WRITER_TYPE_CUSTOMER,
                request.writerName(),
                request.title(),
                request.content(),
                request.detailType(),
                request.isSecret());
    }

    /**
     * 주문 문의 생성 요청을 Command로 변환
     *
     * <p>고객 대신 문의를 생성하는 경우 사용
     *
     * @param request API 요청
     * @return Application Command
     */
    public CreateOrderQnaCommand toCreateOrderQnaCommand(CreateOrderQnaV2ApiRequest request) {
        String writerId = SecurityContextHolder.getCurrentUserId();
        List<String> imageUrls = request.imageUrls() != null ? request.imageUrls() : List.of();

        return new CreateOrderQnaCommand(
                request.orderId(),
                writerId,
                WRITER_TYPE_CUSTOMER,
                request.writerName(),
                request.title(),
                request.content(),
                request.detailType(),
                request.isSecret(),
                imageUrls);
    }

    /**
     * 답변 생성 요청을 Command로 변환
     *
     * <p>관리자가 문의에 답변을 작성하는 경우 사용
     *
     * @param qnaId 문의 ID
     * @param request API 요청
     * @return Application Command
     */
    public CreateQnaReplyCommand toCreateReplyCommand(Long qnaId, CreateQnaReplyV2ApiRequest request) {
        String writerId = SecurityContextHolder.getCurrentUserId();

        return new CreateQnaReplyCommand(
                qnaId,
                request.parentReplyId(),
                writerId,
                WRITER_TYPE_ADMIN,
                DEFAULT_ADMIN_NAME,
                request.content());
    }

    /**
     * 문의 종료 Command 생성
     *
     * @param qnaId 문의 ID
     * @return Application Command
     */
    public CloseQnaCommand toCloseCommand(Long qnaId) {
        return new CloseQnaCommand(qnaId);
    }

    /**
     * 검색 요청을 Query로 변환
     *
     * @param request API 요청
     * @return Application Query
     */
    public QnaSearchQuery toSearchQuery(QnaSearchV2ApiRequest request) {
        return new QnaSearchQuery(
                request.qnaType(),
                request.targetId(),
                request.status(),
                request.writerName(),
                request.sortBy(),
                request.sortDirection(),
                request.page(),
                request.size());
    }
}
