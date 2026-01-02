package com.ryuqq.setof.adapter.out.persistence.qna.adapter;

import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaReplyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.mapper.QnaReplyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.qna.repository.QnaReplyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.qna.repository.QnaReplyQueryDslRepository;
import com.ryuqq.setof.application.qna.port.out.command.QnaReplyPersistencePort;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.exception.QnaReplyNotFoundException;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * QnaReplyPersistenceAdapter - QnA Reply Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, QnA Reply 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>규칙:</strong> Command Adapter는 persist() 단일 메서드로 저장/수정을 처리합니다.
 *
 * <p><strong>Materialized Path 생성:</strong>
 *
 * <ul>
 *   <li>루트 답변: "001", "002", ... (3자리 패딩)
 *   <li>자식 답변: "001.001", "001.002", ... (부모 Path + 자식 번호)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaReplyPersistenceAdapter implements QnaReplyPersistencePort {

    private static final int PATH_SEGMENT_LENGTH = 3;
    private static final String PATH_SEPARATOR = ".";

    private final QnaReplyJpaRepository qnaReplyJpaRepository;
    private final QnaReplyQueryDslRepository qnaReplyQueryDslRepository;
    private final QnaReplyJpaEntityMapper qnaReplyJpaEntityMapper;

    public QnaReplyPersistenceAdapter(
            QnaReplyJpaRepository qnaReplyJpaRepository,
            QnaReplyQueryDslRepository qnaReplyQueryDslRepository,
            QnaReplyJpaEntityMapper qnaReplyJpaEntityMapper) {
        this.qnaReplyJpaRepository = qnaReplyJpaRepository;
        this.qnaReplyQueryDslRepository = qnaReplyQueryDslRepository;
        this.qnaReplyJpaEntityMapper = qnaReplyJpaEntityMapper;
    }

    /**
     * QnA Reply 영속화 (저장/수정)
     *
     * <p>ID가 없으면 신규 저장 (Path 생성 포함), ID가 있으면 수정합니다 (upsert 패턴).
     *
     * @param reply QnaReply 도메인
     * @return 저장된 QnaReplyId
     */
    @Override
    public QnaReplyId persist(QnaReply reply) {
        if (reply.getId() != null) {
            return updateExisting(reply);
        }
        return saveNew(reply);
    }

    private QnaReplyId saveNew(QnaReply reply) {
        String path = generatePath(reply);
        QnaReplyJpaEntity entity = qnaReplyJpaEntityMapper.toEntity(reply, path);
        QnaReplyJpaEntity savedEntity = qnaReplyJpaRepository.save(entity);
        return QnaReplyId.of(savedEntity.getId());
    }

    private QnaReplyId updateExisting(QnaReply reply) {
        Long replyId = reply.getId().getValue();
        QnaReplyJpaEntity entity =
                qnaReplyJpaRepository
                        .findById(replyId)
                        .orElseThrow(() -> new QnaReplyNotFoundException(replyId));

        entity.updateContent(reply.getContent().getContent());
        qnaReplyJpaRepository.save(entity);
        return reply.getId();
    }

    /**
     * Materialized Path 생성
     *
     * @param reply QnaReply 도메인
     * @return 생성된 Path 문자열
     */
    private String generatePath(QnaReply reply) {
        if (reply.isRootReply()) {
            return generateRootPath(reply.getQnaId());
        }
        return generateChildPath(reply);
    }

    /**
     * 루트 답변 Path 생성
     *
     * @param qnaId QnA ID
     * @return 루트 Path (예: "001", "002", ...)
     */
    private String generateRootPath(long qnaId) {
        Optional<String> maxPath = qnaReplyQueryDslRepository.findMaxRootPath(qnaId);
        int nextNumber = maxPath.map(path -> extractLastSegment(path) + 1).orElse(1);
        return formatSegment(nextNumber);
    }

    /**
     * 자식 답변 Path 생성
     *
     * @param reply QnaReply 도메인
     * @return 자식 Path (예: "001.001", "001.002", ...)
     */
    private String generateChildPath(QnaReply reply) {
        // 부모 Path를 DB 형식으로 변환
        String parentDbPath = qnaReplyJpaEntityMapper.toDbPath(reply.getPath());
        Optional<String> maxChildPath =
                qnaReplyQueryDslRepository.findMaxChildPath(reply.getQnaId(), parentDbPath);
        int nextNumber =
                maxChildPath
                        .map(
                                path -> {
                                    String lastSegment = path.substring(path.lastIndexOf(PATH_SEPARATOR) + 1);
                                    return Integer.parseInt(lastSegment) + 1;
                                })
                        .orElse(1);
        return parentDbPath + PATH_SEPARATOR + formatSegment(nextNumber);
    }

    /**
     * Path에서 마지막 세그먼트 숫자 추출
     *
     * @param path Path 문자열
     * @return 마지막 세그먼트 숫자
     */
    private int extractLastSegment(String path) {
        int lastSeparatorIndex = path.lastIndexOf(PATH_SEPARATOR);
        String lastSegment = lastSeparatorIndex == -1 ? path : path.substring(lastSeparatorIndex + 1);
        return Integer.parseInt(lastSegment);
    }

    /**
     * 세그먼트 번호 포맷팅 (3자리 패딩)
     *
     * @param number 세그먼트 번호
     * @return 포맷팅된 문자열 (예: 1 -> "001")
     */
    private String formatSegment(int number) {
        return String.format("%0" + PATH_SEGMENT_LENGTH + "d", number);
    }
}
