package com.ryuqq.setof.adapter.out.persistence.qna.mapper;

import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaJpaEntity;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import com.ryuqq.setof.domain.qna.vo.QnaImage;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.WriterId;
import com.ryuqq.setof.domain.qna.vo.WriterType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

/**
 * QnaJpaEntityMapper - QnA Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Qna 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Qna -> QnaJpaEntity (저장용)
 *   <li>Qna.images -> List&lt;QnaImageJpaEntity&gt; (저장용)
 *   <li>QnaJpaEntity + List&lt;QnaImageJpaEntity&gt; -> Qna (조회용)
 * </ul>
 *
 * <p><strong>시간 변환:</strong>
 *
 * <ul>
 *   <li>Domain: LocalDateTime (KST 기준)
 *   <li>Entity: Instant (UTC 기준)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaJpaEntityMapper {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * Domain -> QnA Entity 변환
     *
     * @param domain Qna 도메인
     * @return QnaJpaEntity
     */
    public QnaJpaEntity toEntity(Qna domain) {
        return QnaJpaEntity.of(
                domain.getId() != null ? domain.getId().getValue() : null,
                domain.getType().name(),
                domain.getDetailType().name(),
                domain.getTargetId(),
                domain.getWriterId().getValue().toString(),
                domain.getWriterType().name(),
                domain.getWriterName(),
                domain.getContent().getTitle(),
                domain.getContent().getContent(),
                domain.isSecret(),
                domain.getStatus().name(),
                domain.getReplyCount(),
                toInstant(domain.getCreatedAt()),
                toInstant(domain.getCreatedAt()),
                toInstant(domain.getDeletedAt()));
    }

    /**
     * Domain images -> Image Entity 목록 변환
     *
     * @param domain Qna 도메인
     * @param qnaId 저장된 QnA ID
     * @return QnaImageJpaEntity 목록
     */
    public List<QnaImageJpaEntity> toImageEntities(Qna domain, Long qnaId) {
        List<QnaImage> images = domain.getImages();
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        Instant now = Instant.now();
        return images.stream()
                .map(
                        image ->
                                QnaImageJpaEntity.of(
                                        null,
                                        qnaId,
                                        image.getImageUrl(),
                                        image.getDisplayOrder(),
                                        now,
                                        now))
                .toList();
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity QnaJpaEntity
     * @param imageEntities 이미지 Entity 목록 (nullable)
     * @return Qna 도메인
     */
    public Qna toDomain(QnaJpaEntity entity, List<QnaImageJpaEntity> imageEntities) {
        List<QnaImage> images = toQnaImages(imageEntities);

        return Qna.reconstitute(
                QnaId.of(entity.getId()),
                QnaType.valueOf(entity.getQnaType()),
                QnaDetailType.valueOf(entity.getDetailType()),
                entity.getTargetId(),
                WriterId.of(UUID.fromString(entity.getWriterId())),
                WriterType.valueOf(entity.getWriterType()),
                entity.getWriterName(),
                QnaContent.of(entity.getTitle(), entity.getContent()),
                entity.isSecret(),
                images,
                QnaStatus.valueOf(entity.getStatus()),
                entity.getReplyCount(),
                toLocalDateTime(entity.getCreatedAt()),
                toLocalDateTime(entity.getDeletedAt()));
    }

    /**
     * Entity -> Domain 변환 (이미지 없음)
     *
     * @param entity QnaJpaEntity
     * @return Qna 도메인
     */
    public Qna toDomain(QnaJpaEntity entity) {
        return toDomain(entity, Collections.emptyList());
    }

    /**
     * Image Entity 목록 -> QnaImage VO 목록 변환
     *
     * @param entities QnaImageJpaEntity 목록
     * @return QnaImage 목록
     */
    private List<QnaImage> toQnaImages(List<QnaImageJpaEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(e -> QnaImage.of(e.getImageUrl(), e.getDisplayOrder()))
                .toList();
    }

    /**
     * LocalDateTime -> Instant 변환 (KST -> UTC)
     *
     * @param localDateTime LocalDateTime (nullable)
     * @return Instant (nullable)
     */
    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(KST_ZONE).toInstant();
    }

    /**
     * Instant -> LocalDateTime 변환 (UTC -> KST)
     *
     * @param instant Instant (nullable)
     * @return LocalDateTime (nullable)
     */
    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, KST_ZONE);
    }
}
