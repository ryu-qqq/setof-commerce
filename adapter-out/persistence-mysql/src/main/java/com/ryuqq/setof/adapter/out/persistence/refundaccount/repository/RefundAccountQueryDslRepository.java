package com.ryuqq.setof.adapter.out.persistence.refundaccount.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.QRefundAccountJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * RefundAccountQueryDslRepository - RefundAccount QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 단건 조회
 *   <li>findByMemberId(String memberId): 회원별 환불계좌 조회 (최대 1개)
 *   <li>existsByMemberId(String memberId): 회원별 환불계좌 존재 여부
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
public class RefundAccountQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QRefundAccountJpaEntity qRefundAccount =
            QRefundAccountJpaEntity.refundAccountJpaEntity;

    public RefundAccountQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 RefundAccount 단건 조회
     *
     * @param id RefundAccount ID
     * @return RefundAccountJpaEntity (Optional)
     */
    public Optional<RefundAccountJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qRefundAccount)
                        .where(qRefundAccount.id.eq(id), isNotDeleted())
                        .fetchOne());
    }

    /**
     * 회원별 환불계좌 조회 (최대 1개)
     *
     * @param memberId 회원 ID (UUID v7 문자열)
     * @return RefundAccountJpaEntity (Optional)
     */
    public Optional<RefundAccountJpaEntity> findByMemberId(String memberId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qRefundAccount)
                        .where(qRefundAccount.memberId.eq(memberId), isNotDeleted())
                        .fetchOne());
    }

    /**
     * 회원별 환불계좌 존재 여부 확인
     *
     * @param memberId 회원 ID (UUID v7 문자열)
     * @return 존재 여부
     */
    public boolean existsByMemberId(String memberId) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qRefundAccount)
                        .where(qRefundAccount.memberId.eq(memberId), isNotDeleted())
                        .fetchFirst();
        return count != null;
    }

    // ========== Private Helper Methods ==========

    /**
     * 삭제되지 않은 레코드 조건
     *
     * @return BooleanExpression (deletedAt IS NULL)
     */
    private BooleanExpression isNotDeleted() {
        return qRefundAccount.deletedAt.isNull();
    }
}
