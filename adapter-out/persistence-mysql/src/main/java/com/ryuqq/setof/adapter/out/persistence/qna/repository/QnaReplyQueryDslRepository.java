package com.ryuqq.setof.adapter.out.persistence.qna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QQnaReplyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaReplyJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * QnaReplyQueryDslRepository - QnA 답변 QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 답변 단건 조회
 *   <li>findByQnaId(Long qnaId): QnA에 속한 답변 목록 조회 (path 순)
 *   <li>countByQnaId(Long qnaId): QnA에 속한 답변 개수 조회
 *   <li>findMaxPathByQnaIdAndParentPath(...): Path 생성용 최대 Path 조회
 * </ul>
 *
 * <p><strong>Materialized Path 패턴:</strong>
 *
 * <ul>
 *   <li>path로 정렬하면 자연스러운 트리 순서
 *   <li>루트 답변: "001", "002", ...
 *   <li>자식 답변: "001.001", "001.002", ...
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class QnaReplyQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QQnaReplyJpaEntity qReply = QQnaReplyJpaEntity.qnaReplyJpaEntity;

    public QnaReplyQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 답변 단건 조회
     *
     * @param id 답변 ID
     * @return QnaReplyJpaEntity (Optional)
     */
    public Optional<QnaReplyJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qReply).where(qReply.id.eq(id), notDeleted()).fetchOne());
    }

    /**
     * ID로 답변 단건 조회 (삭제 포함)
     *
     * @param id 답변 ID
     * @return QnaReplyJpaEntity (Optional)
     */
    public Optional<QnaReplyJpaEntity> findByIdIncludeDeleted(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qReply).where(qReply.id.eq(id)).fetchOne());
    }

    /**
     * QnA ID로 답변 목록 조회 (path 순 정렬)
     *
     * @param qnaId QnA ID
     * @return QnaReplyJpaEntity 목록 (path 순)
     */
    public List<QnaReplyJpaEntity> findByQnaId(Long qnaId) {
        return queryFactory
                .selectFrom(qReply)
                .where(qReply.qnaId.eq(qnaId), notDeleted())
                .orderBy(qReply.path.asc())
                .fetch();
    }

    /**
     * QnA ID로 답변 개수 조회
     *
     * @param qnaId QnA ID
     * @return 답변 개수
     */
    public long countByQnaId(Long qnaId) {
        Long count =
                queryFactory
                        .select(qReply.count())
                        .from(qReply)
                        .where(qReply.qnaId.eq(qnaId), notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 루트 레벨 최대 Path 조회 (루트 답변 Path 생성용)
     *
     * <p>QnA에 속한 루트 답변들 중 가장 큰 path를 조회합니다.
     *
     * @param qnaId QnA ID
     * @return 최대 Path (없으면 null)
     */
    public Optional<String> findMaxRootPath(Long qnaId) {
        String maxPath =
                queryFactory
                        .select(qReply.path.max())
                        .from(qReply)
                        .where(
                                qReply.qnaId.eq(qnaId),
                                qReply.parentReplyId.isNull(),
                                notDeleted())
                        .fetchOne();
        return Optional.ofNullable(maxPath);
    }

    /**
     * 부모 Path 하위 최대 Path 조회 (자식 답변 Path 생성용)
     *
     * <p>부모 Path로 시작하고 부모 Path 바로 아래 레벨의 답변들 중 가장 큰 path를 조회합니다. 예: 부모 Path "001"인 경우, "001.001",
     * "001.002" 중 최대값 조회
     *
     * @param qnaId QnA ID
     * @param parentPath 부모 Path
     * @return 최대 Path (없으면 null)
     */
    public Optional<String> findMaxChildPath(Long qnaId, String parentPath) {
        // 부모 Path 하위 직계 자식만 조회 (손자 제외)
        // 예: parentPath="001" → "001.XXX" 패턴만 (점 개수로 depth 제한)
        int targetDepth = parentPath.split("\\.").length + 1;

        List<String> paths =
                queryFactory
                        .select(qReply.path)
                        .from(qReply)
                        .where(
                                qReply.qnaId.eq(qnaId),
                                qReply.path.startsWith(parentPath + "."),
                                notDeleted())
                        .fetch();

        // 직계 자식 depth인 것만 필터링 후 최대값 조회
        return paths.stream()
                .filter(path -> path.split("\\.").length == targetDepth)
                .max(String::compareTo);
    }

    /**
     * ID로 답변 존재 여부 확인
     *
     * @param id 답변 ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qReply)
                        .where(qReply.id.eq(id), notDeleted())
                        .fetchFirst();
        return count != null;
    }

    /** 삭제되지 않은 조건 */
    private com.querydsl.core.types.dsl.BooleanExpression notDeleted() {
        return qReply.deletedAt.isNull();
    }
}
