package com.ryuqq.setof.adapter.out.persistence.qna.mapper;

import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaReplyJpaEntity;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import com.ryuqq.setof.domain.qna.vo.ReplyContent;
import com.ryuqq.setof.domain.qna.vo.ReplyPath;
import com.ryuqq.setof.domain.qna.vo.ReplyWriterType;
import com.ryuqq.setof.domain.qna.vo.WriterId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * QnaReplyJpaEntityMapper - QnA Reply Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 QnaReply 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>QnaReply -> QnaReplyJpaEntity (저장용)
 *   <li>QnaReplyJpaEntity -> QnaReply (조회용)
 * </ul>
 *
 * <p><strong>Path 구분자 변환:</strong>
 *
 * <ul>
 *   <li>Domain: "/" (ReplyPath VO)
 *   <li>Entity: "." (DB 저장)
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
public class QnaReplyJpaEntityMapper {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
    private static final String DOMAIN_PATH_SEPARATOR = "/";
    private static final String DB_PATH_SEPARATOR = ".";

    /**
     * Domain -> Reply Entity 변환
     *
     * @param domain QnaReply 도메인
     * @param path 저장될 Path (Persistence Layer에서 생성)
     * @return QnaReplyJpaEntity
     */
    public QnaReplyJpaEntity toEntity(QnaReply domain, String path) {
        return QnaReplyJpaEntity.of(
                domain.getId() != null ? domain.getId().getValue() : null,
                domain.getQnaId(),
                domain.getParentReplyId(),
                domain.getWriterId().getValue().toString(),
                domain.getWriterType().name(),
                domain.getWriterName(),
                domain.getContent().getContent(),
                path,
                toInstant(domain.getCreatedAt()),
                toInstant(domain.getCreatedAt()),
                toInstant(domain.getDeletedAt()));
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity QnaReplyJpaEntity
     * @return QnaReply 도메인
     */
    public QnaReply toDomain(QnaReplyJpaEntity entity) {
        return QnaReply.reconstitute(
                QnaReplyId.of(entity.getId()),
                entity.getQnaId(),
                entity.getParentReplyId(),
                WriterId.of(UUID.fromString(entity.getWriterId())),
                ReplyWriterType.valueOf(entity.getWriterType()),
                entity.getWriterName(),
                ReplyContent.of(entity.getContent()),
                toDomainPath(entity.getPath()),
                toLocalDateTime(entity.getCreatedAt()),
                toLocalDateTime(entity.getDeletedAt()));
    }

    /**
     * DB Path -> Domain Path 변환 ("." -> "/")
     *
     * @param dbPath DB에 저장된 Path
     * @return ReplyPath VO
     */
    private ReplyPath toDomainPath(String dbPath) {
        if (dbPath == null) {
            return null;
        }
        String domainPath = dbPath.replace(DB_PATH_SEPARATOR, DOMAIN_PATH_SEPARATOR);
        return ReplyPath.of(domainPath);
    }

    /**
     * Domain Path -> DB Path 변환 ("/" -> ".")
     *
     * @param domainPath Domain의 ReplyPath
     * @return DB용 Path 문자열
     */
    public String toDbPath(ReplyPath domainPath) {
        if (domainPath == null) {
            return null;
        }
        return domainPath.getPath().replace(DOMAIN_PATH_SEPARATOR, DB_PATH_SEPARATOR);
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
