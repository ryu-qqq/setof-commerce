package com.ryuqq.setof.application.qna.assembler;

import com.ryuqq.setof.application.qna.dto.response.QnaImageResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaReplyResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaResponse;
import com.ryuqq.setof.application.qna.dto.response.QnaSummaryResponse;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * QnA Assembler
 *
 * <p>Domain 객체 → Response DTO 변환을 담당
 *
 * <p>주의: toDomain은 CommandFactory 책임입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaAssembler {

    /**
     * Qna 도메인을 QnaResponse로 변환
     *
     * @param qna Qna 도메인 객체
     * @return QnaResponse
     */
    public QnaResponse toQnaResponse(Qna qna) {
        List<QnaImageResponse> images =
                qna.getImages().stream()
                        .map(img -> QnaImageResponse.of(img.getImageUrl(), img.getDisplayOrder()))
                        .toList();

        return QnaResponse.of(
                qna.getId().getValue(),
                qna.getType().name(),
                qna.getDetailType().name(),
                qna.getTargetId(),
                qna.getWriterId().getValue().toString(),
                qna.getWriterType().name(),
                qna.getWriterName(),
                qna.getContent().getTitle(),
                qna.getContent().getContent(),
                qna.isSecret(),
                qna.getStatus().name(),
                qna.getReplyCount(),
                images,
                qna.getCreatedAt());
    }

    /**
     * Qna 도메인을 QnaSummaryResponse로 변환
     *
     * @param qna Qna 도메인 객체
     * @return QnaSummaryResponse
     */
    public QnaSummaryResponse toQnaSummaryResponse(Qna qna) {
        return QnaSummaryResponse.of(
                qna.getId().getValue(),
                qna.getType().name(),
                qna.getDetailType().name(),
                qna.getWriterName(),
                qna.getContent().getTitle(),
                qna.isSecret(),
                qna.getStatus().name(),
                qna.getReplyCount(),
                qna.getCreatedAt());
    }

    /**
     * Qna 도메인 목록을 QnaSummaryResponse 목록으로 변환
     *
     * @param qnas Qna 도메인 목록
     * @return QnaSummaryResponse 목록
     */
    public List<QnaSummaryResponse> toQnaSummaryResponses(List<Qna> qnas) {
        return qnas.stream().map(this::toQnaSummaryResponse).toList();
    }

    /**
     * QnaReply 도메인을 QnaReplyResponse로 변환
     *
     * @param reply QnaReply 도메인 객체
     * @return QnaReplyResponse
     */
    public QnaReplyResponse toQnaReplyResponse(QnaReply reply) {
        return QnaReplyResponse.of(
                reply.getId().getValue(),
                reply.getQnaId(),
                reply.getParentReplyId(),
                reply.getWriterId().getValue().toString(),
                reply.getWriterType().name(),
                reply.getWriterName(),
                reply.getContent().getContent(),
                reply.getPath().getPath(),
                reply.getDepth(),
                reply.getCreatedAt());
    }

    /**
     * QnaReply 도메인 목록을 QnaReplyResponse 목록으로 변환
     *
     * @param replies QnaReply 도메인 목록
     * @return QnaReplyResponse 목록
     */
    public List<QnaReplyResponse> toQnaReplyResponses(List<QnaReply> replies) {
        return replies.stream().map(this::toQnaReplyResponse).toList();
    }
}
