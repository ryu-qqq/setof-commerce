package com.ryuqq.setof.domain.qna.event;

import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.time.LocalDateTime;
import java.util.UUID;

public record QnaCreatedEvent(
        long qnaId,
        QnaType qnaType,
        Long targetId,
        UUID writerId,
        LocalDateTime occurredAt
) {

    public static QnaCreatedEvent of(long qnaId, QnaType qnaType, Long targetId, UUID writerId) {
        return new QnaCreatedEvent(qnaId, qnaType, targetId, writerId, LocalDateTime.now());
    }
}
