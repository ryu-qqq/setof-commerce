package com.ryuqq.setof.storage.legacy.qna.mapper;

import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaAnswer;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaAnswerEntity;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaContents;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaAnswerEntityMapper - Q&A 답변 Domain → Entity 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaAnswerEntityMapper {

    private static final String SYSTEM_OPERATOR = "SYSTEM";
    private static final String WRITER_TYPE_SELLER = "SELLER";

    /**
     * Qna 도메인에서 답변 엔티티를 생성합니다.
     *
     * <p>legacyId가 있으면 reconstitute(UPDATE), 없으면 create(INSERT).
     *
     * @param qnaId 레거시 Q&A ID
     * @param domain Qna 도메인 객체 (answer 포함)
     * @return LegacyQnaAnswerEntity
     */
    public LegacyQnaAnswerEntity toEntity(long qnaId, Qna domain) {
        QnaAnswer answer = domain.answer();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime insertDate = toLocalDateTime(answer.createdAt(), now);
        LocalDateTime updateDate = toLocalDateTime(answer.updatedAt(), now);

        LegacyQnaContents contents =
                new LegacyQnaContents(domain.titleValue(), answer.contentValue());
        LegacyQnaAnswerEntity.Yn deleteYn =
                answer.isDeleted() ? LegacyQnaAnswerEntity.Yn.Y : LegacyQnaAnswerEntity.Yn.N;

        if (answer.legacyId() != null && !answer.legacyId().isNew()) {
            return LegacyQnaAnswerEntity.reconstitute(
                    answer.legacyIdValue(),
                    qnaId,
                    domain.parentIdValue(),
                    WRITER_TYPE_SELLER,
                    domain.status().name(),
                    contents,
                    deleteYn,
                    SYSTEM_OPERATOR,
                    SYSTEM_OPERATOR,
                    insertDate,
                    updateDate);
        }

        return LegacyQnaAnswerEntity.create(
                qnaId,
                domain.parentIdValue(),
                WRITER_TYPE_SELLER,
                domain.status().name(),
                contents,
                deleteYn,
                SYSTEM_OPERATOR,
                SYSTEM_OPERATOR,
                insertDate,
                updateDate);
    }

    private LocalDateTime toLocalDateTime(Instant instant, LocalDateTime fallback) {
        if (instant == null) {
            return fallback;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
