package com.ryuqq.setof.domain.qna.event;

import com.ryuqq.setof.domain.qna.vo.ReplyWriterType;
import java.time.LocalDateTime;
import java.util.UUID;

public record QnaRepliedEvent(
        long qnaId,
        long replyId,
        UUID writerId,
        ReplyWriterType writerType,
        LocalDateTime occurredAt
) {

    public static QnaRepliedEvent of(long qnaId, long replyId, UUID writerId, ReplyWriterType writerType) {
        return new QnaRepliedEvent(qnaId, replyId, writerId, writerType, LocalDateTime.now());
    }
}
