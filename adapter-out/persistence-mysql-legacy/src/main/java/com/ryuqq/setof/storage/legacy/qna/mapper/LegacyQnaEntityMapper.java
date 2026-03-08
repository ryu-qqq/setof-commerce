package com.ryuqq.setof.storage.legacy.qna.mapper;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaAnswer;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
import com.ryuqq.setof.domain.qna.id.LegacyQnaAnswerId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.id.QnaAnswerId;
import com.ryuqq.setof.domain.qna.id.QnaId;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaTitle;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaAnswerEntity;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaContents;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaOrderEntity;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaProductEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaEntityMapper - Q&A Domain → Entity 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaEntityMapper {

    private static final String SYSTEM_OPERATOR = "SYSTEM";
    private static final String USER_TYPE_MEMBER = "MEMBER";

    /**
     * Qna 도메인 객체를 LegacyQnaEntity로 변환합니다.
     *
     * @param domain Qna 도메인 객체
     * @return LegacyQnaEntity
     */
    public LegacyQnaEntity toEntity(Qna domain) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime insertDate = toLocalDateTime(domain.createdAt(), now);
        LocalDateTime updateDate = toLocalDateTime(domain.updatedAt(), now);

        LegacyQnaContents contents =
                new LegacyQnaContents(domain.titleValue(), domain.contentValue());
        LegacyQnaEntity.Yn privateYn =
                domain.isSecret() ? LegacyQnaEntity.Yn.Y : LegacyQnaEntity.Yn.N;
        LegacyQnaEntity.Yn deleteYn =
                domain.isDeleted() ? LegacyQnaEntity.Yn.Y : LegacyQnaEntity.Yn.N;

        if (domain.legacyId().isNew()) {
            return LegacyQnaEntity.create(
                    contents,
                    privateYn,
                    domain.status().name(),
                    domain.qnaType().name(),
                    domain.detailType().name(),
                    domain.legacyMemberIdValue(),
                    domain.sellerId(),
                    USER_TYPE_MEMBER,
                    deleteYn,
                    SYSTEM_OPERATOR,
                    SYSTEM_OPERATOR,
                    insertDate,
                    updateDate);
        }

        return LegacyQnaEntity.reconstitute(
                domain.legacyIdValue(),
                contents,
                privateYn,
                domain.status().name(),
                domain.qnaType().name(),
                domain.detailType().name(),
                domain.legacyMemberIdValue(),
                domain.sellerId(),
                USER_TYPE_MEMBER,
                deleteYn,
                SYSTEM_OPERATOR,
                SYSTEM_OPERATOR,
                insertDate,
                updateDate);
    }

    /**
     * QnaProduct 도메인에서 LegacyQnaProductEntity를 생성합니다.
     *
     * @param domain QnaProduct 도메인 객체 (qnaId 포함)
     * @return LegacyQnaProductEntity
     */
    public LegacyQnaProductEntity toProductEntity(QnaProduct domain) {
        LocalDateTime now = toLocalDateTime(domain.createdAt(), LocalDateTime.now());

        return LegacyQnaProductEntity.create(
                domain.qnaIdValue(),
                domain.productGroupIdValue(),
                Yn.N,
                SYSTEM_OPERATOR,
                SYSTEM_OPERATOR,
                now,
                now);
    }

    /**
     * QnaOrder 도메인에서 LegacyQnaOrderEntity를 생성합니다.
     *
     * @param domain QnaOrder 도메인 객체 (qnaId 포함)
     * @return LegacyQnaOrderEntity
     */
    public LegacyQnaOrderEntity toOrderEntity(QnaOrder domain) {
        LocalDateTime now = toLocalDateTime(domain.createdAt(), LocalDateTime.now());

        return LegacyQnaOrderEntity.create(
                domain.qnaIdValue(),
                domain.legacyOrderIdValue(),
                Yn.N,
                SYSTEM_OPERATOR,
                SYSTEM_OPERATOR,
                now,
                now);
    }

    /**
     * LegacyQnaEntity를 Qna 도메인 객체로 변환합니다.
     *
     * @param entity Q&A 엔티티
     * @param answerEntity 답변 엔티티 (없으면 null)
     * @return Qna 도메인 객체
     */
    public Qna toDomain(LegacyQnaEntity entity, LegacyQnaAnswerEntity answerEntity) {
        QnaAnswer answer = null;
        if (answerEntity != null) {
            answer =
                    QnaAnswer.reconstitute(
                            QnaAnswerId.forNew(),
                            LegacyQnaAnswerId.of(answerEntity.getId()),
                            QnaContent.of(answerEntity.getQnaContents().getContent()),
                            DeletionStatus.active(),
                            toInstant(answerEntity.getInsertDate()),
                            toInstant(answerEntity.getUpdateDate()));
        }

        return Qna.reconstitute(
                QnaId.forNew(),
                LegacyQnaId.of(entity.getId()),
                null,
                LegacyMemberId.of(entity.getUserId()),
                null,
                entity.getSellerId(),
                QnaType.valueOf(entity.getQnaType()),
                QnaDetailType.valueOf(entity.getQnaDetailType()),
                QnaStatus.valueOf(entity.getQnaStatus()),
                QnaTitle.of(entity.getQnaContents().getTitle()),
                QnaContent.of(entity.getQnaContents().getContent()),
                entity.getPrivateYn() == LegacyQnaEntity.Yn.Y,
                answer,
                entity.getDeleteYn() == LegacyQnaEntity.Yn.Y
                        ? DeletionStatus.deletedAt(toInstant(entity.getUpdateDate()))
                        : DeletionStatus.active(),
                toInstant(entity.getInsertDate()),
                toInstant(entity.getUpdateDate()));
    }

    private Instant toInstant(LocalDateTime dateTime) {
        if (dateTime == null) {
            return Instant.now();
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private LocalDateTime toLocalDateTime(Instant instant, LocalDateTime fallback) {
        if (instant == null) {
            return fallback;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
