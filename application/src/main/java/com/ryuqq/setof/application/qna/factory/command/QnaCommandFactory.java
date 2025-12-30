package com.ryuqq.setof.application.qna.factory.command;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ryuqq.setof.application.qna.dto.command.CreateOrderQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateProductQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.CreateQnaReplyCommand;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaImage;
import com.ryuqq.setof.domain.qna.vo.ReplyContent;
import com.ryuqq.setof.domain.qna.vo.ReplyPath;
import com.ryuqq.setof.domain.qna.vo.ReplyWriterType;
import com.ryuqq.setof.domain.qna.vo.WriterId;
import com.ryuqq.setof.domain.qna.vo.WriterType;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

/**
 * QnA Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성 로직 캡슐화
 *   <li>UUIDv7 생성 (WriterId용)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaCommandFactory {

    /**
     * 상품 문의 생성
     *
     * @param command 상품 문의 생성 커맨드
     * @return 생성된 Qna (저장 전, ID 없음)
     */
    public Qna createProductQna(CreateProductQnaCommand command) {
        WriterId writerId = WriterId.forNew(UUID.fromString(command.writerId()));
        WriterType writerType = WriterType.valueOf(command.writerType());
        QnaContent content = QnaContent.of(command.title(), command.content());
        QnaDetailType detailType = QnaDetailType.valueOf(command.detailType());

        return Qna.forNewProductQna(
                command.productGroupId(),
                writerId,
                writerType,
                command.writerName(),
                content,
                detailType,
                command.isSecret());
    }

    /**
     * 주문 문의 생성
     *
     * @param command 주문 문의 생성 커맨드
     * @return 생성된 Qna (저장 전, ID 없음)
     */
    public Qna createOrderQna(CreateOrderQnaCommand command) {
        WriterId writerId = WriterId.forNew(UUID.fromString(command.writerId()));
        WriterType writerType = WriterType.valueOf(command.writerType());
        QnaContent content = QnaContent.of(command.title(), command.content());
        QnaDetailType detailType = QnaDetailType.valueOf(command.detailType());
        List<QnaImage> images = createImages(command.imageUrls());

        return Qna.forNewOrderQna(
                command.orderId(),
                writerId,
                writerType,
                command.writerName(),
                content,
                detailType,
                command.isSecret(),
                images);
    }

    /**
     * 루트 답변 생성
     *
     * @param command 답변 생성 커맨드
     * @return 생성된 QnaReply (저장 전, ID/Path 없음)
     */
    public QnaReply createRootReply(CreateQnaReplyCommand command) {
        WriterId writerId = WriterId.forNew(UUID.fromString(command.writerId()));
        ReplyWriterType writerType = ReplyWriterType.valueOf(command.writerType());
        ReplyContent content = ReplyContent.of(command.content());

        return QnaReply.forNewRootReply(
                command.qnaId(), writerId, writerType, command.writerName(), content);
    }

    /**
     * 대댓글 생성
     *
     * @param command 답변 생성 커맨드
     * @param parentPath 부모 답변 경로
     * @return 생성된 QnaReply (저장 전, ID 없음, Path는 Persistence에서 생성)
     */
    public QnaReply createChildReply(CreateQnaReplyCommand command, ReplyPath parentPath) {
        WriterId writerId = WriterId.forNew(UUID.fromString(command.writerId()));
        ReplyWriterType writerType = ReplyWriterType.valueOf(command.writerType());
        ReplyContent content = ReplyContent.of(command.content());

        return QnaReply.forNewChildReply(
                command.qnaId(),
                command.parentReplyId(),
                parentPath,
                writerId,
                writerType,
                command.writerName(),
                content);
    }

    /**
     * 새 Writer ID 생성 (UUIDv7)
     *
     * @return 새로 생성된 WriterId
     */
    public WriterId generateWriterId() {
        return WriterId.forNew(UuidCreator.getTimeOrderedEpoch());
    }

    private List<QnaImage> createImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return List.of();
        }
        return IntStream.range(0, imageUrls.size())
                .mapToObj(i -> QnaImage.of(imageUrls.get(i), i))
                .toList();
    }
}
