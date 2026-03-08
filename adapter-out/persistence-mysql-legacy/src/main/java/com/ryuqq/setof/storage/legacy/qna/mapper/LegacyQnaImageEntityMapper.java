package com.ryuqq.setof.storage.legacy.qna.mapper;

import com.ryuqq.setof.domain.qna.aggregate.QnaImage;
import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaImageEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaImageEntityMapper - Q&A 이미지 Domain → Entity 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaImageEntityMapper {

    private static final String SYSTEM_OPERATOR = "SYSTEM";
    private static final String QNA_ISSUE_TYPE_QUESTION = "QUESTION";

    /**
     * QnaImage 도메인 객체를 LegacyQnaImageEntity로 변환합니다.
     *
     * @param qnaId 레거시 Q&A ID
     * @param domain QnaImage 도메인 객체
     * @return LegacyQnaImageEntity
     */
    public LegacyQnaImageEntity toEntity(long qnaId, QnaImage domain) {
        LocalDateTime now = toLocalDateTime(domain.createdAt());

        return LegacyQnaImageEntity.create(
                qnaId,
                null,
                QNA_ISSUE_TYPE_QUESTION,
                domain.imageUrl(),
                (long) domain.displayOrder(),
                Yn.N,
                SYSTEM_OPERATOR,
                SYSTEM_OPERATOR,
                now,
                now);
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return LocalDateTime.now();
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
