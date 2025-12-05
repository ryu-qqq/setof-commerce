package com.ryuqq.setof.storage.mysql.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.mysql.member.entity.MemberJpaEntity;
import com.ryuqq.setof.storage.mysql.member.entity.QMemberJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MemberQueryDslRepository - Member QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>UUID v7 기반 PK:</strong>
 *
 * <ul>
 *   <li>id 컬럼이 String 타입 UUID v7
 *   <li>보안성: 순차적 ID 예측 방지
 * </ul>
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(String id): ID(UUID v7)로 단건 조회
 *   <li>existsById(String id): 존재 여부 확인
 *   <li>findByPhoneNumber(String phoneNumber): 핸드폰 번호로 조회
 *   <li>findBySocialId(String socialId): 소셜 ID로 조회
 *   <li>existsByPhoneNumber(String phoneNumber): 핸드폰 번호 존재 여부
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>동적 쿼리 구성 (BooleanExpression)
 *   <li>단건/목록 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>❌ Join 절대 금지 (fetch join, left join, inner join)
 *   <li>❌ 비즈니스 로직 금지
 *   <li>❌ Mapper 호출 금지
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Repository
public class MemberQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QMemberJpaEntity qMember = QMemberJpaEntity.memberJpaEntity;

    public MemberQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID(UUID v7)로 Member 단건 조회
     *
     * @param id Member ID (UUID v7 문자열)
     * @return MemberJpaEntity (Optional)
     */
    public Optional<MemberJpaEntity> findById(String id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qMember)
                        .where(qMember.id.eq(id), isNotDeleted())
                        .fetchOne());
    }

    /**
     * ID(UUID v7)로 Member 존재 여부 확인
     *
     * @param id Member ID (UUID v7 문자열)
     * @return 존재 여부
     */
    public boolean existsById(String id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qMember)
                        .where(qMember.id.eq(id), isNotDeleted())
                        .fetchFirst();

        return count != null;
    }

    /**
     * 휴대폰 번호로 Member 단건 조회
     *
     * @param phoneNumber 휴대폰 번호
     * @return MemberJpaEntity (Optional)
     */
    public Optional<MemberJpaEntity> findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qMember)
                        .where(qMember.phoneNumber.eq(phoneNumber), isNotDeleted())
                        .fetchOne());
    }

    /**
     * 소셜 ID로 Member 단건 조회
     *
     * @param socialId 소셜 ID
     * @return MemberJpaEntity (Optional)
     */
    public Optional<MemberJpaEntity> findBySocialId(String socialId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qMember)
                        .where(qMember.socialId.eq(socialId), isNotDeleted())
                        .fetchOne());
    }

    /**
     * 휴대폰 번호로 Member 존재 여부 확인
     *
     * @param phoneNumber 휴대폰 번호
     * @return 존재 여부
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qMember)
                        .where(qMember.phoneNumber.eq(phoneNumber), isNotDeleted())
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
        return qMember.deletedAt.isNull();
    }
}
