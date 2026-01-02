package com.ryuqq.setof.domain.qna.event;

import java.time.LocalDateTime;

public record QnaClosedEvent(
        long qnaId,
        LocalDateTime occurredAt
) {

    public static QnaClosedEvent of(long qnaId) {
        return new QnaClosedEvent(qnaId, LocalDateTime.now());
    }
}
