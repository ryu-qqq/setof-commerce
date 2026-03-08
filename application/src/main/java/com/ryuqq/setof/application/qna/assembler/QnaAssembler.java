package com.ryuqq.setof.application.qna.assembler;

import com.ryuqq.setof.application.qna.dto.response.MyQnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;
import com.ryuqq.setof.application.qna.dto.response.QnaAnswerDetailResult;
import com.ryuqq.setof.application.qna.dto.response.QnaAnswerResult;
import com.ryuqq.setof.application.qna.dto.response.QnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;
import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * QnaAssembler - Q&A Result DTO 조립.
 *
 * <p>Manager에서 조회한 Result DTO를 Application Result DTO로 조립합니다. 비밀글 마스킹 및 작성자명 마스킹을 적용합니다.
 *
 * <p>마스킹 규칙 (레거시 호환):
 *
 * <ul>
 *   <li>타인 글: userName 끝 2자리 ** 마스킹
 *   <li>타인 비밀글: content → "비밀글 입니다.", answers → 빈 Set
 *   <li>본인 글: 마스킹 없음
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaAssembler {

    private static final String PRIVATE_CONTENT_MASK = "비밀글 입니다.";

    /**
     * 상품 Q&A 페이지 결과 조립 (Offset 기반).
     *
     * @param qnas Q&A 목록
     * @param viewerUserId 조회자 사용자 ID (null이면 비로그인)
     * @param page 현재 페이지 번호
     * @param size 페이지 크기
     * @param totalElements 전체 건수
     * @return QnaPageResult
     */
    public QnaPageResult toQnaPageResult(
            List<QnaWithAnswersResult> qnas,
            Long viewerUserId,
            int page,
            int size,
            long totalElements) {
        List<QnaDetailResult> content =
                qnas.stream().map(qna -> toQnaDetailResult(qna, viewerUserId)).toList();
        return QnaPageResult.of(content, page, size, totalElements);
    }

    /**
     * 내 Q&A 슬라이스 결과 조립 (Cursor 기반).
     *
     * @param qnas 내 Q&A 목록 (fetchSize = requestedSize + 1 결과)
     * @param requestedSize 요청 페이지 크기
     * @return MyQnaSliceResult
     */
    public MyQnaSliceResult toMyQnaSliceResult(
            List<MyQnaResult> qnas, int requestedSize) {
        boolean hasNext = qnas.size() > requestedSize;
        List<MyQnaResult> sliced = hasNext ? qnas.subList(0, requestedSize) : qnas;
        List<MyQnaDetailResult> content =
                sliced.stream().map(this::toMyQnaDetailResult).toList();

        Long lastId = content.isEmpty() ? null : content.getLast().qnaId();
        SliceMeta sliceMeta =
                SliceMeta.withCursor(lastId, requestedSize, hasNext, content.size());

        return MyQnaSliceResult.of(content, sliceMeta);
    }

    private QnaDetailResult toQnaDetailResult(
            QnaWithAnswersResult qna, Long viewerUserId) {
        boolean isOwner = viewerUserId != null && viewerUserId == qna.userId();
        boolean isPrivate = "Y".equalsIgnoreCase(qna.privateYn());

        String maskedUserName = isOwner ? qna.userName() : maskUserName(qna.userName());
        String maskedContent = (!isOwner && isPrivate) ? PRIVATE_CONTENT_MASK : qna.content();
        Set<QnaAnswerDetailResult> answers =
                (!isOwner && isPrivate) ? Set.of() : toAnswerDetailResults(qna.answers());

        return QnaDetailResult.of(
                qna.qnaId(),
                qna.title(),
                maskedContent,
                qna.privateYn(),
                qna.qnaStatus(),
                qna.qnaType(),
                qna.qnaDetailType(),
                qna.userType(),
                qna.userId(),
                maskedUserName,
                qna.insertDate(),
                qna.updateDate(),
                answers);
    }

    private MyQnaDetailResult toMyQnaDetailResult(MyQnaResult qna) {
        Set<QnaAnswerDetailResult> answers = toAnswerDetailResults(qna.answers());

        return MyQnaDetailResult.of(
                qna.qnaId(),
                qna.title(),
                qna.content(),
                qna.privateYn(),
                qna.qnaStatus(),
                qna.qnaType(),
                qna.qnaDetailType(),
                qna.userType(),
                qna.userId(),
                qna.userName(),
                qna.productGroupId(),
                qna.productGroupName(),
                qna.imageUrl(),
                qna.brandId(),
                qna.brandName(),
                qna.orderId(),
                qna.paymentId(),
                qna.orderAmount(),
                qna.quantity(),
                qna.option(),
                qna.insertDate(),
                qna.updateDate(),
                answers);
    }

    private Set<QnaAnswerDetailResult> toAnswerDetailResults(Set<QnaAnswerResult> answerResults) {
        if (answerResults == null) {
            return Set.of();
        }
        return answerResults.stream()
                .map(this::toQnaAnswerDetailResult)
                .collect(Collectors.toSet());
    }

    private QnaAnswerDetailResult toQnaAnswerDetailResult(QnaAnswerResult answer) {
        return QnaAnswerDetailResult.of(
                answer.qnaAnswerId(),
                answer.qnaParentId(),
                answer.qnaWriterType(),
                answer.title(),
                answer.content(),
                answer.insertDate(),
                answer.updateDate());
    }

    /**
     * 작성자명 끝 2자리 ** 마스킹 처리 (레거시 호환).
     *
     * @param userName 원본 작성자명
     * @return 마스킹 처리된 작성자명
     */
    private String maskUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            return "**";
        }
        if (userName.length() <= 2) {
            return "**";
        }
        return userName.substring(0, userName.length() - 2) + "**";
    }
}
